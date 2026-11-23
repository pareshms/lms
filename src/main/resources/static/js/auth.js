// ── auth.js — Login handler ──────────────────────────────────
(function(){
  if(localStorage.getItem('token')){window.location.href='dashboard.html';return;}
})();
document.getElementById('loginForm').addEventListener('submit',async function(e){
  e.preventDefault();
  const err=document.getElementById('loginError');err.style.display='none';
  const username=document.getElementById('username').value.trim();
  const password=document.getElementById('password').value;
  try{
    const r=await fetch('/api/auth/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username,password})});
    const data=await r.json();
    if(r.ok){
      localStorage.setItem('token',data.token);
      localStorage.setItem('userName',data.name);
      localStorage.setItem('role',data.role);
      window.location.href='dashboard.html';
    }else{
      err.textContent=data.error||'Invalid credentials';err.style.display='block';
    }
  }catch(ex){err.textContent='Server unreachable';err.style.display='block';}
});
