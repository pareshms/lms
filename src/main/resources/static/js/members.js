// ── members.js ──────────────────────────────────────────────
document.addEventListener('DOMContentLoaded',loadMembers);
document.getElementById('searchInput').addEventListener('keyup',function(e){if(e.key==='Enter')searchMembers();});

async function loadMembers(){
  var r=await api('/members');renderMembers(await r.json());
}
async function searchMembers(){
  var kw=document.getElementById('searchInput').value.trim();
  if(!kw){loadMembers();return;}
  var r=await api('/members/search?keyword='+encodeURIComponent(kw));renderMembers(await r.json());
}
function renderMembers(list){
  var tb=document.getElementById('membersTable');tb.innerHTML='';
  list.forEach(function(m){
    var dept=m.department||'—';
    var statusBadge=m.active?'<span class="badge badge-green">Active</span>':'<span class="badge badge-gray">Inactive</span>';
    tb.innerHTML+='<tr><td>'+m.id+'</td><td>'+m.name+'</td><td>'+m.email+'</td><td>'+(m.phone||'—')+'</td><td><span class="badge badge-blue">'+m.userType+'</span></td><td>'+dept+'</td><td>'+m.borrowLimit+'</td><td>'+statusBadge+'</td><td><button class="btn btn-sm btn-outline" onclick="editMember('+m.id+')"><i class="fas fa-edit"></i></button> <button class="btn btn-sm btn-warning" onclick="toggleActive('+m.id+')"><i class="fas fa-ban"></i></button> <button class="btn btn-sm btn-primary" onclick="showHistory('+m.id+')"><i class="fas fa-history"></i></button></td></tr>';
  });
  if(!list.length)tb.innerHTML='<tr><td colspan="9" style="text-align:center;color:#999">No members found</td></tr>';
}
function openModal(){document.getElementById('memberForm').reset();document.getElementById('mId').value='';document.getElementById('mType').disabled=false;document.getElementById('modalTitle').textContent='Add Member';toggleTypeFields();document.getElementById('modal').classList.add('active');}
function closeModal(){document.getElementById('modal').classList.remove('active');}
function toggleTypeFields(){
  var t=document.getElementById('mType').value;
  document.getElementById('studentFields').style.display=t==='STUDENT'?'block':'none';
  document.getElementById('facultyFields').style.display=t==='FACULTY'?'block':'none';
}
async function editMember(id){
  var r=await api('/members/'+id);var m=await r.json();
  document.getElementById('mId').value=m.id;
  document.getElementById('mType').value=m.userType;document.getElementById('mType').disabled=true;
  document.getElementById('mName').value=m.name;
  document.getElementById('mEmail').value=m.email;
  document.getElementById('mPhone').value=m.phone||'';
  document.getElementById('mUsername').value=m.username;
  document.getElementById('mPassword').value='';
  document.getElementById('mDept').value=m.department||'';
  if(m.userType==='STUDENT'){document.getElementById('mRoll').value=m.rollNumber||'';document.getElementById('mYear').value=m.year||'';}
  if(m.userType==='FACULTY'){document.getElementById('mEmpId').value=m.employeeId||'';document.getElementById('mDesig').value=m.designation||'';}
  document.getElementById('modalTitle').textContent='Edit Member';
  toggleTypeFields();document.getElementById('modal').classList.add('active');
}
async function saveMember(e){
  e.preventDefault();
  var id=document.getElementById('mId').value;
  var t=document.getElementById('mType').value;
  var body={userType:t,name:document.getElementById('mName').value,email:document.getElementById('mEmail').value,phone:document.getElementById('mPhone').value,username:document.getElementById('mUsername').value,department:document.getElementById('mDept').value};
  var pw=document.getElementById('mPassword').value;if(pw)body.password=pw;else if(!id){showToast('Password required for new member','error');return;}
  if(t==='STUDENT'){body.rollNumber=document.getElementById('mRoll').value;body.year=parseInt(document.getElementById('mYear').value)||null;}
  if(t==='FACULTY'){body.employeeId=document.getElementById('mEmpId').value;body.designation=document.getElementById('mDesig').value;}
  var r;
  if(id){r=await api('/members/'+id,{method:'PUT',body:JSON.stringify(body)});}
  else{r=await api('/members',{method:'POST',body:JSON.stringify(body)});}
  if(r.ok){closeModal();showToast(id?'Member updated':'Member added');loadMembers();}
  else{var d=await r.json();showToast(d.error||'Error','error');}
}
async function toggleActive(id){
  var r=await api('/members/'+id+'/toggle-active',{method:'PUT'});
  if(r.ok){showToast('Status toggled');loadMembers();}
}
async function showHistory(id){
  var r=await api('/members/'+id+'/history');var list=await r.json();
  var tb=document.getElementById('historyTable');tb.innerHTML='';
  list.forEach(function(b){tb.innerHTML+='<tr><td>'+b.book.title+'</td><td>'+fmt(b.issueDate)+'</td><td>'+fmt(b.dueDate)+'</td><td>'+fmt(b.returnDate)+'</td><td>₹'+b.fine.toFixed(2)+'</td><td>'+badge(b.status)+'</td></tr>';});
  if(!list.length)tb.innerHTML='<tr><td colspan="6" style="text-align:center;color:#999">No history</td></tr>';
  document.getElementById('historyModal').classList.add('active');
}
