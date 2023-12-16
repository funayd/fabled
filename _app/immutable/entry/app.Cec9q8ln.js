import{s as q,a as B,e as d,c as U,i as b,d as h,b as j,o as M,f as W,g as z,h as F,j as D,k as p,l as G,m as H,n as J,t as K,p as I,q as k}from"../chunks/scheduler.094yGyYV.js";import{S as Q,i as X,t as g,c as P,a as w,g as L,b as v,d as N,m as E,e as y}from"../chunks/index.Fsxk9AdB.js";const Y="modulepreload",Z=function(o,e){return new URL(o,e).href},O={},R=function(e,n,i){let s=Promise.resolve();if(n&&n.length>0){const c=document.getElementsByTagName("link");s=Promise.all(n.map(t=>{if(t=Z(t,i),t in O)return;O[t]=!0;const r=t.endsWith(".css"),l=r?'[rel="stylesheet"]':"";if(!!i)for(let u=c.length-1;u>=0;u--){const m=c[u];if(m.href===t&&(!r||m.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${t}"]${l}`))return;const a=document.createElement("link");if(a.rel=r?"stylesheet":Y,r||(a.as="script",a.crossOrigin=""),a.href=t,document.head.appendChild(a),r)return new Promise((u,m)=>{a.addEventListener("load",u),a.addEventListener("error",()=>m(new Error(`Unable to preload CSS for ${t}`)))})}))}return s.then(()=>e()).catch(c=>{const t=new Event("vite:preloadError",{cancelable:!0});if(t.payload=c,window.dispatchEvent(t),!t.defaultPrevented)throw c})};function $(o){return/^(skill|class)$/.test(o)}const oe={istype:$};function x(o){let e,n,i;var s=o[1][0];function c(t,r){return{props:{data:t[3],form:t[2]}}}return s&&(e=k(s,c(o)),o[12](e)),{c(){e&&v(e.$$.fragment),n=d()},l(t){e&&N(e.$$.fragment,t),n=d()},m(t,r){e&&E(e,t,r),b(t,n,r),i=!0},p(t,r){if(r&2&&s!==(s=t[1][0])){if(e){L();const l=e;g(l.$$.fragment,1,0,()=>{y(l,1)}),P()}s?(e=k(s,c(t)),t[12](e),v(e.$$.fragment),w(e.$$.fragment,1),E(e,n.parentNode,n)):e=null}else if(s){const l={};r&8&&(l.data=t[3]),r&4&&(l.form=t[2]),e.$set(l)}},i(t){i||(e&&w(e.$$.fragment,t),i=!0)},o(t){e&&g(e.$$.fragment,t),i=!1},d(t){t&&h(n),o[12](null),e&&y(e,t)}}}function ee(o){let e,n,i;var s=o[1][0];function c(t,r){return{props:{data:t[3],$$slots:{default:[te]},$$scope:{ctx:t}}}}return s&&(e=k(s,c(o)),o[11](e)),{c(){e&&v(e.$$.fragment),n=d()},l(t){e&&N(e.$$.fragment,t),n=d()},m(t,r){e&&E(e,t,r),b(t,n,r),i=!0},p(t,r){if(r&2&&s!==(s=t[1][0])){if(e){L();const l=e;g(l.$$.fragment,1,0,()=>{y(l,1)}),P()}s?(e=k(s,c(t)),t[11](e),v(e.$$.fragment),w(e.$$.fragment,1),E(e,n.parentNode,n)):e=null}else if(s){const l={};r&8&&(l.data=t[3]),r&8215&&(l.$$scope={dirty:r,ctx:t}),e.$set(l)}},i(t){i||(e&&w(e.$$.fragment,t),i=!0)},o(t){e&&g(e.$$.fragment,t),i=!1},d(t){t&&h(n),o[11](null),e&&y(e,t)}}}function te(o){let e,n,i;var s=o[1][1];function c(t,r){return{props:{data:t[4],form:t[2]}}}return s&&(e=k(s,c(o)),o[10](e)),{c(){e&&v(e.$$.fragment),n=d()},l(t){e&&N(e.$$.fragment,t),n=d()},m(t,r){e&&E(e,t,r),b(t,n,r),i=!0},p(t,r){if(r&2&&s!==(s=t[1][1])){if(e){L();const l=e;g(l.$$.fragment,1,0,()=>{y(l,1)}),P()}s?(e=k(s,c(t)),t[10](e),v(e.$$.fragment),w(e.$$.fragment,1),E(e,n.parentNode,n)):e=null}else if(s){const l={};r&16&&(l.data=t[4]),r&4&&(l.form=t[2]),e.$set(l)}},i(t){i||(e&&w(e.$$.fragment,t),i=!0)},o(t){e&&g(e.$$.fragment,t),i=!1},d(t){t&&h(n),o[10](null),e&&y(e,t)}}}function S(o){let e,n=o[6]&&T(o);return{c(){e=W("div"),n&&n.c(),this.h()},l(i){e=z(i,"DIV",{id:!0,"aria-live":!0,"aria-atomic":!0,style:!0});var s=F(e);n&&n.l(s),s.forEach(h),this.h()},h(){D(e,"id","svelte-announcer"),D(e,"aria-live","assertive"),D(e,"aria-atomic","true"),p(e,"position","absolute"),p(e,"left","0"),p(e,"top","0"),p(e,"clip","rect(0 0 0 0)"),p(e,"clip-path","inset(50%)"),p(e,"overflow","hidden"),p(e,"white-space","nowrap"),p(e,"width","1px"),p(e,"height","1px")},m(i,s){b(i,e,s),n&&n.m(e,null)},p(i,s){i[6]?n?n.p(i,s):(n=T(i),n.c(),n.m(e,null)):n&&(n.d(1),n=null)},d(i){i&&h(e),n&&n.d()}}}function T(o){let e;return{c(){e=G(o[7])},l(n){e=H(n,o[7])},m(n,i){b(n,e,i)},p(n,i){i&128&&J(e,n[7])},d(n){n&&h(e)}}}function ne(o){let e,n,i,s,c;const t=[ee,x],r=[];function l(a,u){return a[1][1]?0:1}e=l(o),n=r[e]=t[e](o);let _=o[5]&&S(o);return{c(){n.c(),i=B(),_&&_.c(),s=d()},l(a){n.l(a),i=U(a),_&&_.l(a),s=d()},m(a,u){r[e].m(a,u),b(a,i,u),_&&_.m(a,u),b(a,s,u),c=!0},p(a,[u]){let m=e;e=l(a),e===m?r[e].p(a,u):(L(),g(r[m],1,1,()=>{r[m]=null}),P(),n=r[e],n?n.p(a,u):(n=r[e]=t[e](a),n.c()),w(n,1),n.m(i.parentNode,i)),a[5]?_?_.p(a,u):(_=S(a),_.c(),_.m(s.parentNode,s)):_&&(_.d(1),_=null)},i(a){c||(w(n),c=!0)},o(a){g(n),c=!1},d(a){a&&(h(i),h(s)),r[e].d(a),_&&_.d(a)}}}function ie(o,e,n){let{stores:i}=e,{page:s}=e,{constructors:c}=e,{components:t=[]}=e,{form:r}=e,{data_0:l=null}=e,{data_1:_=null}=e;j(i.page.notify);let a=!1,u=!1,m=null;M(()=>{const f=i.page.subscribe(()=>{a&&(n(6,u=!0),K().then(()=>{n(7,m=document.title||"untitled page")}))});return n(5,a=!0),f});function V(f){I[f?"unshift":"push"](()=>{t[1]=f,n(0,t)})}function A(f){I[f?"unshift":"push"](()=>{t[0]=f,n(0,t)})}function C(f){I[f?"unshift":"push"](()=>{t[0]=f,n(0,t)})}return o.$$set=f=>{"stores"in f&&n(8,i=f.stores),"page"in f&&n(9,s=f.page),"constructors"in f&&n(1,c=f.constructors),"components"in f&&n(0,t=f.components),"form"in f&&n(2,r=f.form),"data_0"in f&&n(3,l=f.data_0),"data_1"in f&&n(4,_=f.data_1)},o.$$.update=()=>{o.$$.dirty&768&&i.page.set(s)},[t,c,r,l,_,a,u,m,i,s,V,A,C]}class ae extends Q{constructor(e){super(),X(this,e,ie,ne,q,{stores:8,page:9,constructors:1,components:0,form:2,data_0:3,data_1:4})}}const le=[()=>R(()=>import("../nodes/0.JI0DNHmR.js"),__vite__mapDeps([0,1,2,3,4,5,6,7,8,9,10,11]),import.meta.url),()=>R(()=>import("../nodes/1.oAhiaT77.js"),__vite__mapDeps([12,2,3,1]),import.meta.url),()=>R(()=>import("../nodes/2.2bz8NqQB.js"),__vite__mapDeps([13,2,3,4,1,14]),import.meta.url),()=>R(()=>import("../nodes/3.7RNa2vyO.js"),__vite__mapDeps([15,4,1,2,16,17,3,6,7,5,8,18]),import.meta.url),()=>R(()=>import("../nodes/4.paoq5jEa.js"),__vite__mapDeps([19,4,1,2,16,17,3,6,7,9,10,20]),import.meta.url)],fe=[],ce={"/":[2],"/(app)/[type=istype]/[id]":[3],"/(app)/[type=istype]/[id]/edit":[4]},_e={handleError:({error:o})=>{console.error(o)}};export{ce as dictionary,_e as hooks,oe as matchers,le as nodes,ae as root,fe as server_loads};
function __vite__mapDeps(indexes) {
  if (!__vite__mapDeps.viteFileDeps) {
    __vite__mapDeps.viteFileDeps = ["../nodes/0.JI0DNHmR.js","../chunks/singletons.VgU2Bx9y.js","../chunks/scheduler.094yGyYV.js","../chunks/index.Fsxk9AdB.js","../chunks/store.LLkebHnC.js","../chunks/modal-service.SSbn5Yw5.js","../chunks/Toggle.mFUlCo5m.js","../assets/Toggle.dhmSo8m8.css","../assets/modal-service.PgrmOjfD.css","../chunks/AttributeInput.vdluUblI.js","../assets/AttributeInput.7FRW3_yC.css","../assets/0.CpSGejyA.css","../nodes/1.oAhiaT77.js","../nodes/2.2bz8NqQB.js","../assets/2.f3yvOSLu.css","../nodes/3.7RNa2vyO.js","../chunks/index.qPAo0Isn.js","../chunks/control.pJ1mnnAb.js","../assets/3.TU9phYn3.css","../nodes/4.paoq5jEa.js","../assets/4.vYFOiUCH.css"]
  }
  return indexes.map((i) => __vite__mapDeps.viteFileDeps[i])
}