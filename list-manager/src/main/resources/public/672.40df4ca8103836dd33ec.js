(self.webpackChunklistmanager=self.webpackChunklistmanager||[]).push([[672],{9672:(e,t,s)=>{"use strict";s.r(t),s.d(t,{UserModule:()=>M});var i=s(8583),n=s(3679),r=s(2498),o=s(639),a=s(4551);function l(e,t){if(1&e&&(o.TgZ(0,"div",14),o._uU(1),o.qZA()),2&e){const e=o.oxw();o.xp6(1),o.hij(" ",e.errorMessage," ")}}let u=(()=>{class e{constructor(e,t,s){this.router=e,this.location=t,this.auth=s,this.username="",this.password="",this.errorMessage=null,console.log("AuthComponent initialized")}authenticate(e){e.valid?this.auth.authenticate(this.username,this.password).subscribe(e=>{console.log("Response: "),console.log(e),e?this.router.navigateByUrl("/user/main"):this.errorMessage="Authentication Failed"}):this.errorMessage="Form Data Invalid"}goBack(){this.location.back()}}return e.\u0275fac=function(t){return new(t||e)(o.Y36(r.F0),o.Y36(i.Ye),o.Y36(a.e))},e.\u0275cmp=o.Xpm({type:e,selectors:[["ng-component"]],decls:23,vars:3,consts:[[1,"contrainer-fluid"],[1,"bg-info","p-2","text-center","text-white"],[1,"h1"],["class","bg-danger mt-2 p-2 text-center text-dark",4,"ngIf"],[1,"p-2"],["novalidate","",1,"myForm",3,"ngSubmit"],["form","ngForm"],[1,"form-group"],[1,"text-dark"],["name","username","required","",1,"form-control",3,"ngModel","ngModelChange"],["type","password","name","password","required","",1,"form-control",3,"ngModel","ngModelChange"],[1,"text-center"],["routerLink","/",1,"btn","btn-secondary","m-1"],["type","submit",1,"btn","btn-primary","m-1"],[1,"bg-danger","mt-2","p-2","text-center","text-dark"]],template:function(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"div",0),o.TgZ(1,"div",1),o.TgZ(2,"h3",2),o._uU(3,"ListManager User"),o.qZA(),o.TgZ(4,"h3",2),o._uU(5,"Authorization"),o.qZA(),o.qZA(),o.YNc(6,l,2,1,"div",3),o.TgZ(7,"div",4),o.TgZ(8,"form",5,6),o.NdJ("ngSubmit",function(){o.CHM(e);const s=o.MAs(9);return t.authenticate(s)}),o.TgZ(10,"div",7),o.TgZ(11,"label",8),o._uU(12,"Name"),o.qZA(),o.TgZ(13,"input",9),o.NdJ("ngModelChange",function(e){return t.username=e}),o.qZA(),o.qZA(),o.TgZ(14,"div",7),o.TgZ(15,"label",8),o._uU(16,"Password"),o.qZA(),o.TgZ(17,"input",10),o.NdJ("ngModelChange",function(e){return t.password=e}),o.qZA(),o.qZA(),o.TgZ(18,"div",11),o.TgZ(19,"button",12),o._uU(20,"Go back"),o.qZA(),o.TgZ(21,"button",13),o._uU(22,"Log In"),o.qZA(),o.qZA(),o.qZA(),o.qZA(),o.qZA()}2&e&&(o.xp6(6),o.Q6J("ngIf",null!=t.errorMessage),o.xp6(7),o.Q6J("ngModel",t.username),o.xp6(4),o.Q6J("ngModel",t.password))},directives:[i.O5,n._Y,n.JL,n.F,n.Fj,n.Q7,n.JJ,n.On,r.rH],styles:[".myForm[_ngcontent-%COMP%]{min-width:500px;position:absolute;text-align:center;top:50%;left:50%;transform:translate(-50%,-50%);font-size:2.5rem}@media (max-width: 500px){.myForm[_ngcontent-%COMP%]{min-width:90%}}"]}),e})();var g=s(8355),c=s(4345),h=s(5758),d=s(4402),p=s(8002),m=s(5377);let Z=(()=>{class e{constructor(e){this.dataSource=e,this.lists=[],this.categories=[],this.allCategories=[],this.itemStatuses=[],this.roles=[],this.user=null,this.users=[],this.publicSelectedUser=null,this.listTotalCount=0,this.listTotalPageCount=0,this.listPageIndex=0,this.listPageSize=0,this.userTotalCount=0,this.userTotalPageCount=0,this.userPageIndex=0,this.userPageSize=0,console.log("AdminRepository INIT"),this.setUpData(),this.requestUsers()}setUpData(){this.requestAll().subscribe(e=>{this.user=e[0],this.itemStatuses=e[1],this.allCategories=e[2],this.roles=e[3],console.log("Requesting init data")})}requestAll(){let e=[];return e.push(this.dataSource.getUser()),e.push(this.dataSource.getItemStatus()),e.push(this.dataSource.getCategories()),e.push(this.dataSource.getRoles()),(0,h.D)(e)}requestLists(e,t="",s="",i="id",n=0,r=4){return null===this.user?(console.log("$> requestLists: user not set"),(0,d.D)([!1])):this.dataSource.getListsWithParamsUsingUserId(e,t,s,i,n,r).pipe((0,p.U)(e=>{var t;this.listTotalCount=e.totalCount,this.listTotalPageCount=e.totalPageCount,this.listPageIndex=e.pageIndex,this.listPageSize=e.pageSize,this.lists=[];for(let s of e.data){let e=f(s);e.category=null!==(t=this.allCategories.find(e=>e.id==s.category))&&void 0!==t?t:new g.W,this.lists.push(e)}return!0}))}requestUsers(e=null,t="id",s=0,i=4){return null===this.user?(console.log("$> requestUsers: user not set"),(0,d.D)([!1])):this.dataSource.getUsersWithParams(e,t,s,i).pipe((0,p.U)(e=>(this.userTotalCount=e.totalCount,this.userTotalPageCount=e.totalPageCount,this.userPageIndex=e.pageIndex,this.userPageSize=e.pageSize,this.users=e.data,!0)))}getLists(){return this.lists}getUsers(){return this.users}getCategories(){return this.allCategories}getRoles(){return this.roles}getItemStatuses(){return this.itemStatuses}getUser(){return this.user}saveList(e){return this.dataSource.saveUsersList(e).pipe((0,p.U)(e=>this.user?null!==e&&(console.log("Saved list"),console.log(e),!0):(console.log("REST DATASOURCE: user not set!!!"),!1)))}deleteList(e){return this.dataSource.deleteUsersList(e).pipe((0,p.U)(t=>(t&&(this.lists=this.lists.filter(t=>t.id!=e)),t)))}}return e.\u0275fac=function(t){return new(t||e)(o.LFG(m.U))},e.\u0275prov=o.Yz7({token:e,factory:e.\u0275fac}),e})();function f(e){let t=new c.a;return t.id=e.id,t.name=e.name,t.items=e.items,t}let b=(()=>{class e{constructor(e,t,s){this.router=e,this.location=t,this.auth=s,this.user=null,console.log("UserComponent initialized"),this.auth.getUser().subscribe(e=>{e||(e={id:0,username:"Unknown",email:"Unknown",lists:[]}),this.user=e})}}return e.\u0275fac=function(t){return new(t||e)(o.Y36(r.F0),o.Y36(i.Ye),o.Y36(a.e))},e.\u0275cmp=o.Xpm({type:e,selectors:[["ng-component"]],decls:12,vars:3,consts:[[1,"container-fluid"],[1,"bg-info","p-2","text-white","text-center"],["routerLink","/",1,"btn","btn-secondary","m-1"]],template:function(e,t){1&e&&(o.TgZ(0,"div",0),o.TgZ(1,"div",1),o.TgZ(2,"h3"),o._uU(3,"User home page"),o.qZA(),o.TgZ(4,"h3"),o._uU(5),o.qZA(),o.TgZ(6,"h3"),o._uU(7),o.qZA(),o.TgZ(8,"h3"),o._uU(9),o.qZA(),o.TgZ(10,"button",2),o._uU(11,"Go to Main Page"),o.qZA(),o.qZA(),o.qZA()),2&e&&(o.xp6(5),o.hij("Name: ",t.user?t.user.username:"Not loaded",""),o.xp6(2),o.hij("Email: ",t.user?t.user.email:"Not loaded",""),o.xp6(2),o.hij("You have ",t.user?null==t.user.lists?null:t.user.lists.length:"Not loaded"," lists"))},directives:[r.rH],encapsulation:2}),e})();function q(e,t){if(1&e&&(o.TgZ(0,"option",9),o._uU(1),o.qZA()),2&e){const e=t.$implicit;o.Q6J("value",e),o.xp6(1),o.hij(" ",e.name," ")}}function U(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"button",23),o.NdJ("click",function(){const t=o.CHM(e).$implicit;return o.oxw().changeUser(t)}),o._uU(1),o.qZA()}if(2&e){const e=t.$implicit,s=o.oxw();o.ekj("active",e==s.selectedUser)("disabled",s.requestingData),o.xp6(1),o.hij(" ",e.username," ")}}function v(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"button",24),o.NdJ("click",function(){const t=o.CHM(e).$implicit;return o.oxw().changeUserPage(t)}),o._uU(1),o.qZA()}if(2&e){const e=t.$implicit,s=o.oxw();o.ekj("disabled",s.requestingLists)("active",e==s.selectedUserPage),o.xp6(1),o.hij(" ",e," ")}}function A(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"div",25),o.NdJ("click",function(){const t=o.CHM(e).$implicit;return o.oxw().selectList(t)}),o.TgZ(1,"h4"),o._uU(2),o.TgZ(3,"span",26),o._uU(4),o.qZA(),o.qZA(),o.TgZ(5,"div",27),o._uU(6," Embedded list content will go here "),o.qZA(),o.qZA()}if(2&e){const e=t.$implicit;o.xp6(2),o.hij(" ",e.name," "),o.xp6(2),o.hij(" ",null==e.items?null:e.items.length," ")}}function T(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"button",28),o.NdJ("click",function(){const t=o.CHM(e).$implicit;return o.oxw().changeListPage(t)}),o._uU(1),o.qZA()}if(2&e){const e=t.$implicit,s=o.oxw();o.ekj("disabled",s.requestingLists)("active",e==s.selectedListPage),o.xp6(1),o.hij(" ",e," ")}}function x(e,t){if(1&e&&(o.TgZ(0,"div",31),o.TgZ(1,"div",1),o.TgZ(2,"div",32),o._uU(3),o.qZA(),o.TgZ(4,"div",33),o._uU(5),o.qZA(),o.TgZ(6,"div",34),o._uU(7),o.qZA(),o.qZA(),o.qZA()),2&e){const e=t.$implicit;o.xp6(1),o.ekj("btn-outline-warning","To Do"==(null==e.status?null:e.status.name))("btn-outline-info","In Progress"==(null==e.status?null:e.status.name))("btn-outline-success","Done"==(null==e.status?null:e.status.name)),o.xp6(2),o.hij(" ",e.number," "),o.xp6(2),o.hij(" ",e.description," "),o.xp6(2),o.hij(" ",null==e.status?null:e.status.name," ")}}function P(e,t){if(1&e&&(o.TgZ(0,"div",29),o.TgZ(1,"div",1),o.TgZ(2,"h2"),o._uU(3),o.qZA(),o.TgZ(4,"h4"),o._uU(5),o.qZA(),o.YNc(6,x,8,9,"div",30),o.qZA(),o.qZA()),2&e){const e=o.oxw();o.xp6(3),o.hij(" Name: ",e.selectedList.name," "),o.xp6(2),o.hij(" Category: ",null==e.selectedList.category?null:e.selectedList.category.name," "),o.xp6(1),o.Q6J("ngForOf",e.selectedList.items)}}let w=(()=>{class e{constructor(e,t,s,i){this.router=e,this.location=t,this.repository=s,this.auth=i,this.user=null,this.selectedCategory=null,this.listsPerPage=4,this.usersPerPage=4,this.selectedListPage=1,this.selectedUserPage=1,this.listName="",this.requestingLists=!1,this.usersLists=!1,this.requestingData=!1,this.searchUserName="",this.selectedUser=null,this.selectedRole=null,this.selectedRoleId=0,this.selectedList=null,console.log("AdminComponent initialized"),this.auth.getUser().subscribe(e=>{e||(e={id:0,username:"Unknown",roles:[],email:"Unknown",lists:[]}),this.user=e}),this.refreshUsers()}get Roles(){return this.repository.getRoles()}get Users(){return this.repository.getUsers()}changeUserPage(e){this.selectedUserPage=e,this.refreshUsers()}changeUserPageSize(e){this.usersPerPage=Number(e),this.selectedUserPage=1,this.refreshUsers()}changeRole(e){this.selectedRole=null!=e?e:null,this.selectedUserPage=1,this.refreshUsers()}changeUser(e){this.selectedUser=null!=e?e:null,this.selectedListPage=1,this.refreshLists()}refreshUsers(){var e;this.requestingData=!0,this.repository.requestUsers(null===(e=this.selectedRole)||void 0===e?void 0:e.id,"id",this.selectedUserPage-1,this.usersPerPage).subscribe(e=>{this.requestingData=!1,console.log(`Users requestsed, success: ${e}`)})}get userPageCount(){return this.repository.userTotalPageCount}get userPageNumbers(){return Array(this.userPageCount).fill(0).map((e,t)=>t+1)}get Lists(){return this.repository.getLists()}changeListPage(e){this.selectedListPage=e,this.refreshLists()}changeListPageSize(e){this.listsPerPage=Number(e),this.selectedListPage=1,this.refreshLists()}get listPageCount(){return this.repository.listTotalPageCount}get listPageNumbers(){return Array(this.listPageCount).fill(0).map((e,t)=>t+1)}refreshLists(){var e,t;null!==this.selectedUser&&(this.requestingData=!0,this.repository.requestLists(null===(e=this.selectedUser)||void 0===e?void 0:e.id,"",null!==(t=this.selectedCategory)&&void 0!==t?t:void 0,"id",this.selectedListPage-1,this.listsPerPage).subscribe(e=>{this.requestingData=!1,console.log(`Lists requestsed, success: ${e}`)}))}selectList(e){this.selectedList=e}}return e.\u0275fac=function(t){return new(t||e)(o.Y36(r.F0),o.Y36(i.Ye),o.Y36(Z),o.Y36(a.e))},e.\u0275cmp=o.Xpm({type:e,selectors:[["ng-component"]],decls:42,vars:14,consts:[[1,"contrainer-fluid","d-flex","flex-column","h-100"],[1,"row"],[1,"col","bg-dark","text-white"],["routerLink","/",1,"btn","btn-secondary","m-1"],[1,"row","h-100","align-self-stretch"],[1,"col-3","p-2"],[1,"p-2","text-center"],[1,"form-inline","float-left","mr-1","mt-auto"],[1,"form-control",3,"ngModel","change","ngModelChange"],[3,"value"],[3,"value",4,"ngFor","ngForOf"],["class","btn btn-outline-dark btn-block w-100",3,"active","disabled","click",4,"ngFor","ngForOf"],[1,"form-control",3,"value","change"],["value","3"],["value","4"],["value","6"],["value","8"],[1,"btn-group","float-right"],["class","btn btn-outline-dark",3,"disabled","active","click",4,"ngFor","ngForOf"],[1,"col-5","bg-secondary","p-2","h-100","d-flex","flex-column","pb-3"],["class","card m-1 p-1 bg-light",3,"click",4,"ngFor","ngForOf"],["class","btn btn-outline-light",3,"disabled","active","click",4,"ngFor","ngForOf"],["class","col-4 p-2 h-100 d-flex flex-column pb-3",4,"ngIf"],[1,"btn","btn-outline-dark","btn-block","w-100",3,"click"],[1,"btn","btn-outline-dark",3,"click"],[1,"card","m-1","p-1","bg-light",3,"click"],[1,"badge","badge-pill","badge-primary","float-right","text-warning",2,"float","right"],[1,"card-text","bg-white","p-1"],[1,"btn","btn-outline-light",3,"click"],[1,"col-4","p-2","h-100","d-flex","flex-column","pb-3"],["clas","container-fluid",4,"ngFor","ngForOf"],["clas","container-fluid"],[1,"col-1"],[1,"col-7"],[1,"col-4"]],template:function(e,t){1&e&&(o.TgZ(0,"div",0),o.TgZ(1,"div",1),o.TgZ(2,"div",2),o.TgZ(3,"button",3),o._uU(4," Go to main page "),o.qZA(),o.qZA(),o.qZA(),o.TgZ(5,"div",4),o.TgZ(6,"div",5),o.TgZ(7,"h4",6),o._uU(8,"Users:"),o.qZA(),o.TgZ(9,"div",7),o.TgZ(10,"select",8),o.NdJ("change",function(e){return t.changeRole(e.target.value)})("ngModelChange",function(e){return t.selectedRole=e}),o.TgZ(11,"option",9),o._uU(12," All "),o.qZA(),o.YNc(13,q,2,2,"option",10),o.qZA(),o.qZA(),o.YNc(14,U,2,5,"button",11),o.TgZ(15,"div",7),o.TgZ(16,"select",12),o.NdJ("change",function(e){return t.changeUserPageSize(e.target.value)}),o.TgZ(17,"option",13),o._uU(18,"3 per Page"),o.qZA(),o.TgZ(19,"option",14),o._uU(20,"4 per Page"),o.qZA(),o.TgZ(21,"option",15),o._uU(22,"6 per Page"),o.qZA(),o.TgZ(23,"option",16),o._uU(24,"8 per Page"),o.qZA(),o.qZA(),o.qZA(),o.TgZ(25,"div",17),o.YNc(26,v,2,5,"button",18),o.qZA(),o.qZA(),o.TgZ(27,"div",19),o.YNc(28,A,7,2,"div",20),o.TgZ(29,"div",7),o.TgZ(30,"select",12),o.NdJ("change",function(e){return t.changeListPageSize(e.target.value)}),o.TgZ(31,"option",13),o._uU(32,"3 per Page"),o.qZA(),o.TgZ(33,"option",14),o._uU(34,"4 per Page"),o.qZA(),o.TgZ(35,"option",15),o._uU(36,"6 per Page"),o.qZA(),o.TgZ(37,"option",16),o._uU(38,"8 per Page"),o.qZA(),o.qZA(),o.qZA(),o.TgZ(39,"div",17),o.YNc(40,T,2,5,"button",21),o.qZA(),o.qZA(),o.YNc(41,P,7,3,"div",22),o.qZA(),o.qZA()),2&e&&(o.xp6(10),o.Q6J("ngModel",t.selectedRole),o.xp6(1),o.Q6J("value",null),o.xp6(2),o.Q6J("ngForOf",t.Roles),o.xp6(1),o.Q6J("ngForOf",t.Users),o.xp6(2),o.ekj("disabled",t.requestingData),o.Q6J("value",t.usersPerPage),o.xp6(10),o.Q6J("ngForOf",t.userPageNumbers),o.xp6(2),o.Q6J("ngForOf",t.Lists),o.xp6(2),o.ekj("disabled",t.requestingLists),o.Q6J("value",t.listsPerPage),o.xp6(10),o.Q6J("ngForOf",t.listPageNumbers),o.xp6(1),o.Q6J("ngIf",null!==t.selectedList))},directives:[r.rH,n.EJ,n.JJ,n.On,n.YN,n.Kr,i.sg,i.O5],encapsulation:2}),e})(),C=(()=>{class e{constructor(e,t){this.router=e,this.auth=t}canActivate(e,t){return(this.auth.isAdmin||"/user/admin"!=t.url)&&!(!this.auth.authenticated&&"/user/register"!=t.url)||(this.router.navigateByUrl("/user/auth"),!1)}}return e.\u0275fac=function(t){return new(t||e)(o.LFG(r.F0),o.LFG(a.e))},e.\u0275prov=o.Yz7({token:e,factory:e.\u0275fac}),e})();function L(e,t){if(1&e&&(o.TgZ(0,"div",14),o._uU(1),o.qZA()),2&e){const e=o.oxw();o.xp6(1),o.hij(" ",e.ErroMessage," ")}}let y=r.Bz.forChild([{path:"auth",component:u},{path:"register",component:(()=>{class e{constructor(e,t,s){this.router=e,this.location=t,this.auth=s,this.username="",this.password="",this.errorMessage=null,console.log("RegisterComponent initialized")}get ErroMessage(){return this.errorMessage}register(e){e.valid?this.auth.register(this.username,this.password).subscribe(e=>{console.log("Response: "),console.log(e),e?this.router.navigateByUrl("/user/main"):this.errorMessage="Registration Failed (Maybe Name already exists)"}):this.errorMessage="Form Data Invalid"}}return e.\u0275fac=function(t){return new(t||e)(o.Y36(r.F0),o.Y36(i.Ye),o.Y36(a.e))},e.\u0275cmp=o.Xpm({type:e,selectors:[["ng-component"]],decls:23,vars:3,consts:[[1,"contrainer-fluid"],[1,"bg-info","form-header","p-2","text-center","text-white"],[1,"h1"],["class","bg-danger mt-2 p-2 text-center text-dark",4,"ngIf"],[1,"p-2"],["novalidate","",1,"myForm",3,"ngSubmit"],["form","ngForm"],[1,"form-group"],[1,"text-dark"],["name","username","required","",1,"form-control",3,"ngModel","ngModelChange"],["type","password","name","password","required","",1,"form-control",3,"ngModel","ngModelChange"],[1,"text-center"],["routerLink","/",1,"btn","btn-secondary","m-1"],["type","submit",1,"btn","btn-primary","m-1"],[1,"bg-danger","mt-2","p-2","text-center","text-dark"]],template:function(e,t){if(1&e){const e=o.EpF();o.TgZ(0,"div",0),o.TgZ(1,"div",1),o.TgZ(2,"h3",2),o._uU(3,"ListManager User"),o.qZA(),o.TgZ(4,"h3",2),o._uU(5,"Registration"),o.qZA(),o.qZA(),o.YNc(6,L,2,1,"div",3),o.TgZ(7,"div",4),o.TgZ(8,"form",5,6),o.NdJ("ngSubmit",function(){o.CHM(e);const s=o.MAs(9);return t.register(s)}),o.TgZ(10,"div",7),o.TgZ(11,"label",8),o._uU(12,"Name"),o.qZA(),o.TgZ(13,"input",9),o.NdJ("ngModelChange",function(e){return t.username=e}),o.qZA(),o.qZA(),o.TgZ(14,"div",7),o.TgZ(15,"label",8),o._uU(16,"Password"),o.qZA(),o.TgZ(17,"input",10),o.NdJ("ngModelChange",function(e){return t.password=e}),o.qZA(),o.qZA(),o.TgZ(18,"div",11),o.TgZ(19,"button",12),o._uU(20,"Go back"),o.qZA(),o.TgZ(21,"button",13),o._uU(22,"Register"),o.qZA(),o.qZA(),o.qZA(),o.qZA(),o.qZA()}2&e&&(o.xp6(6),o.Q6J("ngIf",null!=t.ErroMessage),o.xp6(7),o.Q6J("ngModel",t.username),o.xp6(4),o.Q6J("ngModel",t.password))},directives:[i.O5,n._Y,n.JL,n.F,n.Fj,n.Q7,n.JJ,n.On,r.rH],styles:[".myForm[_ngcontent-%COMP%]{min-width:500px;position:absolute;text-align:center;top:50%;left:50%;transform:translate(-50%,-50%);font-size:2.5rem;margin:0 auto}@media (max-width: 500px){.myForm[_ngcontent-%COMP%]{min-width:90%}}.form-header[_ngcontent-%COMP%]{font-size:3rem}"]}),e})()},{path:"main",component:b,canActivate:[C]},{path:"admin",component:w,canActivate:[C]},{path:"**",redirectTo:"auth"}]),M=(()=>{class e{}return e.\u0275fac=function(t){return new(t||e)},e.\u0275mod=o.oAB({type:e}),e.\u0275inj=o.cJS({providers:[C,Z],imports:[[i.ez,n.u5,y]]}),e})()}}]);