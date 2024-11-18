import FabledComponent                                from './fabled-component.svelte';
import type { ComponentOption }                       from '../options/options';
import type { ComponentData, Unknown, YamlComponent } from '$api/types';
import Registry                                       from '$api/components/registry';

export default class FabledCondition extends FabledComponent {
	iconKey = $state('');

	public constructor(data: ComponentData) {
		super('condition', data);
	}

	public override changed() {
		return {
			...super.changed(),
			iconKey: this.iconKey
		};
	}

	public override getData(raw = false): Unknown {
		const data: Unknown = {};
		data['icon-key']    = this.iconKey;

		this.data
			.filter(opt => raw || opt.meetsRequirements(this))
			.forEach((opt: ComponentOption) => {
				const optData: { [key: string]: unknown } = opt.getData();
				Object.keys(optData).forEach(key => data[key] = optData[key]);
			});

		return data;
	}

	public override deserialize(yaml: YamlComponent): void {
		super.deserialize(yaml);
		const data = yaml.data;

		this.iconKey = <string>data['icon-key'];

		if (data) this.data.forEach((opt: ComponentOption) => opt.deserialize(data));

		if (yaml.children && Object.keys(yaml.children).length > 0) {
			this.setComponents(Registry.deserializeComponents(yaml.children));
		}
	}

	public static override new = (): FabledCondition => new FabledCondition({ name: 'null' });
}