const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["../nodes/0.BGTdg-8m.js","../chunks/entry.Bo-ciqca.js","../chunks/utils.zETNLfH2.js","../chunks/components.svelte.DHOLmK5P.js","../chunks/index-client.Cq_m8h8c.js","../chunks/disclose-version.Ca3QTfmr.js","../assets/components.C9_E0FrC.css","../chunks/svelte-component.WBZUxysB.js","../chunks/squish.QrUD6Zhr.js","../assets/squish.DFVTWVMT.css","../chunks/lifecycle.DrC_1UrR.js","../chunks/select.t8twCDZL.js","../assets/0.CKCPft4v.css","../nodes/1.5tIPG8V6.js","../nodes/2.DPy2-eG8.js","../assets/2.BDCHogmD.css","../nodes/3.x6b9j5MH.js","../chunks/Control.CcX6J8f_.js","../assets/Control.BCbq7Vr9.css","../assets/3.EmPvmdLe.css","../nodes/4.BH8oX4fd.js","../assets/4.BnaqcHnh.css"])))=>i.map(i=>d[i]);
var B=n=>{throw TypeError(n)};var U=(n,t,r)=>t.has(n)||B("Cannot "+r);var i=(n,t,r)=>(U(n,t,"read from private field"),r?r.call(n):t.get(n)),O=(n,t,r)=>t.has(n)?B("Cannot add the same private member more than once"):t instanceof WeakSet?t.add(n):t.set(n,r),A=(n,t,r,o)=>(U(n,t,"write to private field"),o?o.call(n,r):t.set(n,r),r);import{g as v,D as x,a7 as J,a8 as K,a3 as Q,n as X,u as Y,a as Z,a9 as M,o as k,q as N,aa as S,w as $,s as tt,t as et,v as rt,f as C}from"../chunks/utils.zETNLfH2.js";import{g as st,m as nt,u as at,i as D,a as P,t as z,j as ot,b as it}from"../chunks/disclose-version.Ca3QTfmr.js";import{p as T,o as ct,b as lt,a as I,c as V}from"../chunks/index-client.Cq_m8h8c.js";import{c as j}from"../chunks/svelte-component.WBZUxysB.js";function ut(n){return class extends dt{constructor(t){super({component:n,...t})}}}var g,u;class dt{constructor(t){O(this,g);O(this,u);var _;var r=new Map,o=(s,e)=>{var d=Q(e);return r.set(s,d),d};const c=new Proxy({...t.props||{},$$events:{}},{get(s,e){return v(r.get(e)??o(e,Reflect.get(s,e)))},has(s,e){return v(r.get(e)??o(e,Reflect.get(s,e))),Reflect.has(s,e)},set(s,e,d){return x(r.get(e)??o(e,d),d),Reflect.set(s,e,d)}});A(this,u,(t.hydrate?st:nt)(t.component,{target:t.target,props:c,context:t.context,intro:t.intro??!1,recover:t.recover})),(!((_=t==null?void 0:t.props)!=null&&_.$$host)||t.sync===!1)&&J(),A(this,g,c.$$events);for(const s of Object.keys(i(this,u)))s==="$set"||s==="$destroy"||s==="$on"||K(this,s,{get(){return i(this,u)[s]},set(e){i(this,u)[s]=e},enumerable:!0});i(this,u).$set=s=>{Object.assign(c,s)},i(this,u).$destroy=()=>{at(i(this,u))}}$set(t){i(this,u).$set(t)}$on(t,r){i(this,g)[t]=i(this,g)[t]||[];const o=(...c)=>r.call(this,...c);return i(this,g)[t].push(o),()=>{i(this,g)[t]=i(this,g)[t].filter(c=>c!==o)}}$destroy(){i(this,u).$destroy()}}g=new WeakMap,u=new WeakMap;const mt="modulepreload",ft=function(n,t){return new URL(n,t).href},W={},R=function(t,r,o){let c=Promise.resolve();if(r&&r.length>0){const s=document.getElementsByTagName("link"),e=document.querySelector("meta[property=csp-nonce]"),d=(e==null?void 0:e.nonce)||(e==null?void 0:e.getAttribute("nonce"));c=Promise.allSettled(r.map(l=>{if(l=ft(l,o),l in W)return;W[l]=!0;const y=l.endsWith(".css"),p=y?'[rel="stylesheet"]':"";if(!!o)for(let m=s.length-1;m>=0;m--){const h=s[m];if(h.href===l&&(!y||h.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${l}"]${p}`))return;const a=document.createElement("link");if(a.rel=y?"stylesheet":mt,y||(a.as="script"),a.crossOrigin="",a.href=l,d&&a.setAttribute("nonce",d),document.head.appendChild(a),y)return new Promise((m,h)=>{a.addEventListener("load",m),a.addEventListener("error",()=>h(new Error(`Unable to preload CSS for ${l}`)))})}))}function _(s){const e=new Event("vite:preloadError",{cancelable:!0});if(e.payload=s,window.dispatchEvent(e),!e.defaultPrevented)throw s}return c.then(s=>{for(const e of s||[])e.status==="rejected"&&_(e.reason);return t().catch(_)})};function ht(n){return/^(skill|class|attribute)$/.test(n)}const kt={istype:ht};var _t=z('<div id="svelte-announcer" aria-live="assertive" aria-atomic="true" style="position: absolute; left: 0; top: 0; clip: rect(0 0 0 0); clip-path: inset(50%); overflow: hidden; white-space: nowrap; width: 1px; height: 1px"><!></div>'),vt=z("<!> <!>",1);function gt(n,t){X(t,!0);let r=T(t,"components",23,()=>[]),o=T(t,"data_0",3,null),c=T(t,"data_1",3,null);Y(()=>t.stores.page.set(t.page)),Z(()=>{t.stores,t.page,t.constructors,r(),t.form,o(),c(),t.stores.page.notify()});let _=S(!1),s=S(!1),e=S(null);ct(()=>{const b=t.stores.page.subscribe(()=>{v(_)&&(x(s,!0),M().then(()=>{x(e,lt(document.title||"untitled page"))}))});return x(_,!0),b});const d=C(()=>t.constructors[1]);var l=vt(),y=k(l);I(y,()=>t.constructors[1],b=>{var a=D();const m=C(()=>t.constructors[0]);var h=k(a);j(h,()=>v(m),(E,L)=>{V(L(E,{get data(){return o()},get form(){return t.form},children:(f,yt)=>{var q=D(),F=k(q);j(F,()=>v(d),(G,H)=>{V(H(G,{get data(){return c()},get form(){return t.form}}),w=>r()[1]=w,()=>{var w;return(w=r())==null?void 0:w[1]})}),P(f,q)},$$slots:{default:!0}}),f=>r()[0]=f,()=>{var f;return(f=r())==null?void 0:f[0]})}),P(b,a)},b=>{var a=D();const m=C(()=>t.constructors[0]);var h=k(a);j(h,()=>v(m),(E,L)=>{V(L(E,{get data(){return o()},get form(){return t.form}}),f=>r()[0]=f,()=>{var f;return(f=r())==null?void 0:f[0]})}),P(b,a)});var p=$(y,2);I(p,()=>v(_),b=>{var a=_t(),m=tt(a);I(m,()=>v(s),h=>{var E=ot();et(()=>it(E,v(e))),P(h,E)}),rt(a),P(b,a)}),P(n,l),N()}const xt=ut(gt),pt=[()=>R(()=>import("../nodes/0.BGTdg-8m.js"),__vite__mapDeps([0,1,2,3,4,5,6,7,8,9,10,11,12]),import.meta.url),()=>R(()=>import("../nodes/1.5tIPG8V6.js"),__vite__mapDeps([13,5,2,10,1]),import.meta.url),()=>R(()=>import("../nodes/2.DPy2-eG8.js"),__vite__mapDeps([14,5,2,10,4,3,1,6,15]),import.meta.url),()=>R(()=>import("../nodes/3.x6b9j5MH.js"),__vite__mapDeps([16,3,1,2,4,5,6,17,18,8,9,7,19]),import.meta.url),()=>R(()=>import("../nodes/4.BH8oX4fd.js"),__vite__mapDeps([20,3,1,2,4,5,6,17,18,11,21]),import.meta.url)],Lt=[],Ot={"/":[2],"/(app)/[type=istype]/[id]":[3],"/(app)/[type=istype]/[id]/edit":[4]},At={handleError:({error:n})=>{console.error(n)},reroute:()=>{}};export{Ot as dictionary,At as hooks,kt as matchers,pt as nodes,xt as root,Lt as server_loads};