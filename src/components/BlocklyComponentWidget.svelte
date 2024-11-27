<script lang="ts" module>
	// note: module for Blockly initialization
	import * as Blockly from 'blockly';
	import * as Shadow from '@blockly/shadow-block-converter';
	import { registerFieldMultilineInput, FieldMultilineInput } from '@blockly/field-multilineinput';
	import * as Registry from '$api/components/registry';
	import { get } from 'svelte/store';
	import type { ComponentOption } from '$api/options/options';

	import DropdownSelect from '$api/options/dropdownselect.svelte';
	import BlockSelect from '$api/options/blockselect.svelte';
	import StringSelect from '$api/options/stringselect.svelte';
	import DoubleSelect from '$api/options/doubleselect.svelte';
	import IntSelect from '$api/options/intselect.svelte';
	import StringListSelect from '$api/options/stringlistselect.svelte';
	import BooleanSelect from '$api/options/booleanselect.svelte';

	import '$api/components/toolbox-search';

	const workspace_config = {
		collapse: true,
		comments: true,
		disable: false,
		maxBlocks: Infinity,
		trashcan: true,
		horizontalLayout: false,
		toolboxPosition: 'start',
		css: true,
		media: 'https://blockly-demo.appspot.com/static/media/',
		rtl: false,
		scrollbars: true,
		sounds: true,
		oneBasedIndex: true,
		zoom: {
			controls: false,
			wheel: true,
			startScale: 1,
			maxScale: 3,
			minScale: 0.3,
			scaleSpeed: 1.01
		},
		grid: {
			spacing: 20,
			length: 0,
			colour: '#555',
			snap: true
		}
	};

	const Toolbox = {
		isInitialized() {
			return document.getElementById('toolbox') !== null;
		},

		get() {
			let toolbox = document.getElementById('toolbox');
			if (!toolbox) {
				toolbox = document.createElement('toolbox');
				toolbox.setAttribute('id', 'toolbox');
				toolbox.style.display = 'none';

				document.body.appendChild(toolbox);
			}
			return toolbox;
		},
		update(workspace: Blockly.WorkspaceSvg) {
			workspace.updateToolbox(this.get());
		},
		addSeparator() {
			const separator = document.createElement('sep');
			this.get().appendChild(separator);
		},
		clear() {
			this.get().innerHTML = '';
		}
	};

	function getColor(type: string) {
		switch (type.toLowerCase()) {
			case 'trigger':
				return '#0083ef';
			case 'condition':
				return '#feac00';
			case 'target':
				return '#04af38';
			case 'mechanic':
				return '#ff3a3a';
			default:
				return '#fff';
		}
	}

	class Category {
		constructor(
			public name: string,
			color?: string
		) {
			if (color) this.color = color;
		}

		set color(value: string) {
			this.get().setAttribute('colour', value);
		}

		get() {
			const toolbox = Toolbox.get();
			let category = toolbox.querySelector(`category[name="${this.name}"]`);
			if (!category) {
				category = document.createElement('category');
				category.setAttribute('name', this.name);
				toolbox.appendChild(category);
			}
			return category;
		}

		add(name: string, data?: any) {
			const category = this.get();
			const block = document.createElement('block');
			block.setAttribute('type', name);
			category.appendChild(block);
			if (data && Object.keys(data).length > 0) {
				const elements = parseBlockData(data);
				elements.forEach((element) => block.appendChild(element));
			}
		}
	}

	function parseBlockData(data: any) {
		const elements: HTMLElement[] = [];
		Object.entries(data).forEach(([key, value]) => {
			if (typeof value === 'string') {
				const fieldElement = document.createElement('field');
				fieldElement.setAttribute('name', key);
				fieldElement.textContent = value;
				elements.push(fieldElement);
			} else {
				const valueElement = document.createElement('value');
				valueElement.setAttribute('name', key);
				const { type, ...data } = value as { type: string; [key: string]: any };
				const shadow = document.createElement('shadow');
				shadow.setAttribute('type', type);
				valueElement.appendChild(shadow);
				if (data && Object.keys(data).length > 0) {
					const childElements = parseBlockData(data);
					childElements.forEach((element) => shadow.appendChild(element));
				}
				elements.push(valueElement);
			}
		});
		return elements;
	}

	function setupStyle() {
		const theme = Blockly.Themes.Classic;
		const componentStyles = theme.componentStyles;
		componentStyles.workspaceBackgroundColour = '#111';
		componentStyles.flyoutBackgroundColour = '#181818';
		componentStyles.toolboxBackgroundColour = '#222';
		componentStyles.toolboxForegroundColour = '#ddd';
		componentStyles.flyoutForegroundColour = '#bbb';
		componentStyles.scrollbarColour = '#fff';
		componentStyles.scrollbarOpacity = 0.5;
		Blockly.Scrollbar.scrollbarThickness = 10;
	}

	const cache: string[] = [];

	function migrateRegistry() {
		// @ts-ignore
		const getClassName = (option: ComponentOption) => option.__proto__.constructor.name;
		const capitalize = (str: string) => str.charAt(0).toUpperCase() + str.slice(1);
		const optionToInput = (block: Blockly.Block, option: ComponentOption, title = true, endRow = true, prevInput: Blockly.Input | null = null) => {
			const type = getClassName(option);
			if (endRow) block.appendEndRowInput();
			let input: Blockly.Input = prevInput ?? block.appendDummyInput(option.key);
			if (title) input.appendField(option.name ?? capitalize(option.key));
			switch (type) {
				case 'DropdownSelect':
					const dropdownOption = option as DropdownSelect;
					dropdownOption.init();
					let data = dropdownOption.data as {
						selected: string | string[];
						value: string[];
						multiple: boolean;
					};
					if (data.value.length === 0) {
						console.log(`DropdownSelect ${option.key} has no options`);
						console.log(data);
						break;
					}
					input.appendField(
						new Blockly.FieldDropdown((data.value as string[]).map((option) => [option, option])),
						option.key
					);
					break;
				case 'BlockSelect':
					const blockSelectOption = option as BlockSelect;
					input.appendField("<< BlockSelect >>");
					break;
				case 'StringSelect':
					const stringSelectOption = option as StringSelect;
					input.appendField(new Blockly.FieldTextInput(stringSelectOption.data), option.key);
					break;
				case 'DoubleSelect':
					const doubleSelectOption = option as DoubleSelect;
					input.appendField(new Blockly.FieldNumber(doubleSelectOption.data), option.key);
					break;
				case 'IntSelect':
					const intSelectOption = option as IntSelect;
					const intField = new Blockly.FieldNumber(intSelectOption.data, null, null, null, (n: string | number) => {
						return Number.isInteger(n) ? n : null;
					});
					input.appendField(intField, option.key);
					break;
				case 'StringListSelect':
					const stringListSelectOption = option as StringListSelect;
					input.appendField(new FieldMultilineInput(stringListSelectOption.data.value.join('\n')), option.key);
					break;
				case 'BooleanSelect':
					const booleanSelectOption = option as BooleanSelect;
					input.appendField(
						new Blockly.FieldCheckbox(
							booleanSelectOption.data.toString().toUpperCase() as 'TRUE' | 'FALSE'
						),
						option.key
					);
					break;
			}
			return input;
		};	

		const migrate = (
			type: string,
			name: string,
			color: string,
			registry: Registry.RegistryData
		) => {
			const category = new Category(name, color);
			Blockly.common.defineBlocks(
				Object.entries(registry)
					.map(([key, value]) => {
						key = `${type}_${key}`;
						// @ts-ignore
						const component = new value.component();
						const definition: any = {
							init: function () {
								this.componentType = type;
								const thisBlock = this as Blockly.Block;
								if (!type.startsWith('trigger')) {
									thisBlock.setPreviousStatement(true, null);
									thisBlock.setNextStatement(true, null);
								}
								thisBlock.setColour(color);
								thisBlock.setTooltip(component.description); // todo: create a custom tooltip display to compatible with @html rendering
								const nameInput = thisBlock.appendDummyInput().appendField(component.name);
								const componentOptions = component.data;
								if (componentOptions.length == 1) {
									optionToInput(this, componentOptions[0], false, false, nameInput);
								} else {
									for (const option of componentOptions) optionToInput(this, option);
								}
								thisBlock.appendStatementInput('CHILDREN').setCheck(null);
							},
							onchange: function (event: Blockly.Events.Abstract) {
								const thisBlock = this as Blockly.Block;
								if (!thisBlock.isInFlyout) {
									const rootBlock = thisBlock.getRootBlock();
									thisBlock.setDisabledReason(!rootBlock.type.startsWith('trigger'), 'Block must be connected to a Trigger');
								}
							}
						};
						return [key, definition] as [string, any];
					})
					.reduce((acc: { [key: string]: any }, [key, definition]) => {
						acc[key] = definition;
						category.add(key);
						return acc;
					}, {})
			);
		};

		const triggerGroup: { [key: string]: Registry.RegistryData } = {};
		Object.entries(get(Registry.triggers)).forEach(([key, value]) => {	
			const group = value.section ?? 'General';
			if (!triggerGroup[group]) triggerGroup[group] = {};
			triggerGroup[group][key] = value;
		});
		Object.entries(triggerGroup).forEach(([group, registry]) => {
			migrate(`trigger_${group}`, `${group} Triggers`, getColor('Trigger'), registry);
		});

		Toolbox.addSeparator();

		migrate('target', 'Targeters', getColor('Target'), get(Registry.targets));
		migrate('condition', 'Conditions', getColor('Condition'), get(Registry.conditions));

		const mechanicGroups: { [key: string]: Registry.RegistryData } = {};
		Object.entries(get(Registry.mechanics)).forEach(([key, value]) => {
			const group = value.section ?? 'General';
			if (!mechanicGroups[group]) mechanicGroups[group] = {};
			mechanicGroups[group][key] = value;
		});
		Object.entries(mechanicGroups).forEach(([group, registry]) => {
			migrate(`mechanic_${group}`, `${group} Mechanics`, getColor('Mechanic'), registry);
		});
	}

	function setupToolbox() {
		if (Toolbox.isInitialized()) return;
		registerFieldMultilineInput();

		Toolbox.get().appendChild(
			(() => {
				const searchBox = document.createElement('search');
				searchBox.setAttribute('name', 'Search');
				searchBox.setAttribute('colour', '#5577ee');
				return searchBox;
			})()
		);
		Toolbox.addSeparator();
			
		migrateRegistry();
	}

	
	export function componentToBlock(component: FabledComponent) {
		
	}
</script>

<script lang="ts">
	import BlocklyComponentWidget from './BlocklyComponentWidget.svelte';

	import type FabledComponent from '$api/components/fabled-component.svelte';
	import FabledSkill, { skillStore } from '../data/skill-store.svelte';

	interface Props {
		skill: FabledSkill;
		onsave?: () => void;
		onupdate?: () => void;
		onaddskill?: (e: {
			comp: FabledComponent;
			relativeTo: FabledComponent;
			above: boolean;
		}) => void;
	}

	let { skill, onsave, onupdate, onaddskill }: Props = $props();
	let workspace: Blockly.WorkspaceSvg;

	function blocklyInit(node: HTMLElement) {
		Blockly.ShortcutRegistry.registry.reset();
		setupToolbox();
		setupStyle();
		workspace = Blockly.inject(node, {
			toolbox: Toolbox.get(),
			...workspace_config
		});
		workspace.addChangeListener(Shadow.shadowBlockConversionChangeListener);
	}
</script>

{#key skill}
	<div style="height: 100%; width: 100%;" use:blocklyInit></div>
{/key}

<style>
	/* :global(.blocklyMainWorkspaceScrollbar) {
		display: none;
	}
	:global(.blocklyFlyoutScrollbar) {
		display: none;
	} */
	:global(.blocklyMainBackground) {
		stroke-width: 0;
	}
	:global(.blocklyTreeRowContentContainer input:focus-visible) {
		outline: none;
	}
	:global(.blocklyTreeSeparator) {
		border-bottom: solid #979797 1px;
	}
</style>
