// ── common.js — Shared utilities for all pages ──────────────
(function(){
  if(!localStorage.getItem('token')){window.location.href='login.html';return;}
  var u=document.getElementById('userName');if(u)u.textContent=localStorage.getItem('userName')||'User';
  var lb=document.getElementById('logoutBtn');if(lb)lb.addEventListener('click',function(){localStorage.clear();window.location.href='login.html';});
  var tb=document.getElementById('sidebarToggle');var sb=document.getElementById('sidebar');
  if(tb&&sb)tb.addEventListener('click',function(){sb.classList.toggle('open');});
  // set active link
  var page=location.pathname.split('/').pop()||'dashboard.html';
  document.querySelectorAll('.nav-link').forEach(function(a){a.classList.toggle('active',a.getAttribute('href')===page);});
})();
// API helper
async function api(path,opts){
  opts=opts||{};
  var h=Object.assign({'Content-Type':'application/json'},opts.headers||{});
  var t=localStorage.getItem('token');if(t)h['Authorization']='Bearer '+t;
  var r=await fetch('/api'+path,Object.assign({},opts,{headers:h}));
  if(r.status===401){localStorage.clear();location.href='login.html';}
  return r;
}
function fmt(d){if(!d)return'—';return new Date(d).toLocaleDateString('en-IN',{day:'2-digit',month:'short',year:'numeric'});}
function badge(s){
  if(s==='Available'||s==='RETURNED')return'<span class="badge badge-green">'+s+'</span>';
  if(s==='ISSUED')return'<span class="badge badge-blue">Issued</span>';
  if(s==='OVERDUE')return'<span class="badge badge-red">Overdue</span>';
  if(s==='Issued')return'<span class="badge badge-blue">Issued</span>';
  return'<span class="badge badge-gray">'+s+'</span>';
}
function showToast(msg,type){
  var t=document.createElement('div');t.className='toast toast-'+(type||'success');
  t.innerHTML='<i class="fas fa-'+(type==='error'?'exclamation-circle':'check-circle')+'"></i><span>'+msg+'</span>';
  document.body.appendChild(t);setTimeout(function(){t.classList.add('show')},10);
  setTimeout(function(){t.classList.remove('show');setTimeout(function(){t.remove()},300)},3000);
}
