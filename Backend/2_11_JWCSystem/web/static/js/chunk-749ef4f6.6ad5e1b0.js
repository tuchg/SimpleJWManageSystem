(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-749ef4f6"],{"137c":function(t,e,n){"use strict";var s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{staticClass:"link--mallki",class:t.className,attrs:{href:"#"}},[t._v("\n  "+t._s(t.text)+"\n  "),n("span",{attrs:{"data-letters":t.text}}),t._v(" "),n("span",{attrs:{"data-letters":t.text}})])},a=[],o={props:{className:{type:String,default:""},text:{type:String,default:"vue-element-admin"}}},r=o,i=(n("8c05"),n("2877")),l=Object(i["a"])(r,s,a,!1,null,null,null);e["a"]=l.exports},"1be7":function(t,e,n){},2017:function(t,e,n){"use strict";var s=n("b12d"),a=n.n(s);a.a},"2bef":function(t,e,n){"use strict";var s=n("1be7"),a=n.n(s);a.a},"5f26":function(t,e,n){},"81ca":function(t,e,n){"use strict";var s=n("5f26"),a=n.n(s);a.a},"8c05":function(t,e,n){"use strict";var s=n("b948"),a=n.n(s);a.a},"9ed6":function(t,e,n){"use strict";n.r(e);var s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"login-container"},[n("div",{attrs:{id:"particles"}}),t._v(" "),n("div",{staticClass:"title-container"},[n("h3",{staticClass:"title"},[n("el-image",{staticStyle:{width:"1.5em",height:"1.5em","vertical-align":"middle","margin-right":"10px"},attrs:{src:"https://wpimg.wallstcn.com/69a1c46c-eb1c-4b46-8bd4-e9e686ef5251.png"}}),t._v("\n            教务管理系统\n        ")],1)]),t._v(" "),n("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:t.loginForm,rules:t.loginRules,autocomplete:"on","label-position":"left"}},[n("el-form-item",{attrs:{prop:"username"}},[n("span",{staticClass:"svg-container"},[n("svg-icon",{attrs:{"icon-class":"user"}})],1),t._v(" "),n("el-input",{ref:"username",attrs:{autocomplete:"on",name:"username",placeholder:"用户名",tabindex:"1",type:"text"},model:{value:t.loginForm.username,callback:function(e){t.$set(t.loginForm,"username",e)},expression:"loginForm.username"}})],1),t._v(" "),n("el-tooltip",{attrs:{content:"大写字母键已打开",manual:"",placement:"right"},model:{value:t.capsTooltip,callback:function(e){t.capsTooltip=e},expression:"capsTooltip"}},[n("el-form-item",{attrs:{prop:"password"}},[n("span",{staticClass:"svg-container"},[n("svg-icon",{attrs:{"icon-class":"password"}})],1),t._v(" "),n("el-input",{key:t.passwordType,ref:"password",attrs:{type:t.passwordType,autocomplete:"on",name:"password",placeholder:"密码",tabindex:"2"},on:{blur:function(e){t.capsTooltip=!1}},nativeOn:{keyup:[function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.handleLogin(e)},function(e){return t.checkCapslock(e)}]},model:{value:t.loginForm.password,callback:function(e){t.$set(t.loginForm,"password",e)},expression:"loginForm.password"}}),t._v(" "),n("span",{staticClass:"show-pwd",on:{click:t.showPwd}},[n("svg-icon",{attrs:{"icon-class":"password"===t.passwordType?"eye":"eye-open"}})],1)],1)],1),t._v(" "),n("el-form-item",{attrs:{align:"left"}},[n("span",{staticClass:"svg-container"},[n("svg-icon",{attrs:{"icon-class":"example"}})],1),t._v(" "),n("el-radio-group",{staticStyle:{padding:"12px 5px 12px 15px"},model:{value:t.loginForm.role,callback:function(e){t.$set(t.loginForm,"role",e)},expression:"loginForm.role"}},[n("el-radio",{attrs:{label:"student"}},[t._v("学生")]),t._v(" "),n("el-radio",{attrs:{label:"teacher"}},[t._v("教师")]),t._v(" "),n("el-radio",{attrs:{label:"admin"}},[t._v("管理员")])],1)],1),t._v(" "),n("div",{staticClass:"text-center"},[n("el-button",{staticClass:"pan-btn blue-btn",staticStyle:{width:"50%"},attrs:{loading:t.loading,type:"primary"},nativeOn:{click:function(e){return e.preventDefault(),t.handleLogin(e)}}},[t._v("登录\n            ")])],1)],1),t._v(" "),n("my-footer")],1)},a=[],o=(n("ac6a"),n("456d"),n("137c")),r=n("a3d9"),i=(n("3022"),n("572f"),{name:"Login",components:{MyFooter:r["a"],Mallki:o["a"]},data:function(){var t=function(t,e,n){e.length<3?n(new Error("密码至少要3位")):n()};return{loginForm:{username:"admin",password:"111111",role:"student"},loginRules:{username:[{required:!0,trigger:"blur",message:"请输入用户名"}],password:[{required:!0,trigger:"blur",validator:t}]},passwordType:"password",capsTooltip:!1,loading:!1,showDialog:!1,redirect:void 0,otherQuery:{}}},watch:{$route:{handler:function(t){var e=t.query;e&&(this.redirect=e.redirect,this.otherQuery=this.getOtherQuery(e))},immediate:!0}},created:function(){},mounted:function(){""===this.loginForm.username?this.$refs.username.focus():""===this.loginForm.password&&this.$refs.password.focus(),particlesJS.load("particles","particlesjs-config.json")},destroyed:function(){},methods:{checkCapslock:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},e=t.shiftKey,n=t.key;n&&1===n.length&&(this.capsTooltip=!!(e&&n>="a"&&n<="z"||!e&&n>="A"&&n<="Z")),"CapsLock"===n&&!0===this.capsTooltip&&(this.capsTooltip=!1)},showPwd:function(){var t=this;"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((function(){t.$refs.password.focus()}))},handleLogin:function(){var t=this;this.$refs.loginForm.validate((function(e){if(!e)return console.log("登录失败!"),!1;t.loading=!0,t.$store.dispatch("user/login",t.loginForm).then((function(){t.$router.push({path:t.redirect||"/",queryPage:t.otherQuery}),t.loading=!1})).catch((function(e){console.log(e),t.loading=!1}))}))},getOtherQuery:function(t){return Object.keys(t).reduce((function(e,n){return"redirect"!==n&&(e[n]=t[n]),e}),{})}}}),l=i,c=(n("2017"),n("81ca"),n("2877")),u=Object(c["a"])(l,s,a,!1,null,"2896cf54",null);e["default"]=u.exports},a3d9:function(t,e,n){"use strict";var s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"footer"}},[t._v("\n    © 计软 2018 - 2 - 11\n    "),n("br"),t._v(" "),n("div",[t._v("\n        基于\n        "),n("el-link",{attrs:{target:"_blank",type:"primary",href:"https://panjiachen.github.io/vue-admin-template/",underline:""}},[t._v("\n            vue-element-admin\n        ")]),t._v("\n        修改制作\n    ")],1)])},a=[],o={},r=o,i=(n("2bef"),n("2877")),l=Object(i["a"])(r,s,a,!1,null,"357402cd",null);e["a"]=l.exports},b12d:function(t,e,n){},b948:function(t,e,n){}}]);