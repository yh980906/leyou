webpackJsonp([11],{P7ry:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{username:"",password:"",dialog:!1,e1:!1}},methods:{doLogin:function(){if(!this.username||!this.password)return this.dialog=!0,!1;console.log(this.username+" ... "+this.password),this.$router.push("/")}}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("v-app",[r("v-content",[r("v-container",{attrs:{fluid:"","fill-height":""}},[r("v-layout",{attrs:{"align-center":"","justify-center":""}},[r("v-flex",{attrs:{xs12:"",sm8:"",md4:""}},[r("v-card",{staticClass:"elevation-12"},[r("v-toolbar",{attrs:{dark:"",color:"primary"}},[r("v-toolbar-title",[e._v("乐优商城后台管理")]),e._v(" "),r("v-spacer")],1),e._v(" "),r("v-card-text",[r("v-form",[r("v-text-field",{attrs:{"prepend-icon":"person",label:"用户名",type:"text"},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),e._v(" "),r("v-text-field",{attrs:{"prepend-icon":"lock",label:"密码",id:"password","append-icon":e.e1?"visibility":"visibility_off","append-icon-cb":function(){return e.e1=!e.e1},type:e.e1?"text":"password"},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}})],1)],1),e._v(" "),r("v-card-actions",[r("v-spacer"),e._v(" "),r("v-btn",{attrs:{color:"primary"},on:{click:e.doLogin}},[e._v("登录")])],1)],1)],1)],1)],1)],1),e._v(" "),r("v-dialog",{attrs:{width:"300px"},model:{value:e.dialog,callback:function(t){e.dialog=t},expression:"dialog"}},[r("v-alert",{attrs:{icon:"warning",color:"error",value:!0}},[e._v("\n    用户名和密码不能为空\n    ")])],1)],1)},staticRenderFns:[]},s=r("VU/8")(a,o,!1,null,null,null);t.default=s.exports}});
//# sourceMappingURL=11.eabaddf762c4eb1ed51b.js.map