// ── issue.js ────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded',init);
async function init(){await loadDropdowns();await loadActive();}

async function loadDropdowns(){
  var mr=await api('/members');var members=await mr.json();
  var ms=document.getElementById('memberSelect');
  members.filter(function(m){return m.active}).forEach(function(m){ms.innerHTML+='<option value="'+m.id+'">'+m.name+' ('+m.userType+')</option>';});
  var br=await api('/books');var books=await br.json();
  var bs=document.getElementById('bookSelect');
  books.filter(function(b){return b.availableQuantity>0}).forEach(function(b){bs.innerHTML+='<option value="'+b.id+'">'+b.title+' ('+b.availableQuantity+' avail)</option>';});
}
async function loadActive(){
  var r=await api('/borrows/active');var list=await r.json();
  var tb=document.getElementById('activeTable');tb.innerHTML='';
  list.forEach(function(b){
    tb.innerHTML+='<tr><td>'+b.id+'</td><td>'+b.user.name+'</td><td>'+b.book.title+'</td><td>'+fmt(b.issueDate)+'</td><td>'+fmt(b.dueDate)+'</td><td>'+badge(b.status)+'</td><td>₹'+b.fine.toFixed(2)+'</td><td><button class="btn btn-sm btn-success" onclick="returnBook('+b.id+')"><i class="fas fa-undo"></i> Return</button></td></tr>';
  });
  if(!list.length)tb.innerHTML='<tr><td colspan="8" style="text-align:center;color:#999">No active borrows</td></tr>';
}
async function issueBook(e){
  e.preventDefault();
  var userId=document.getElementById('memberSelect').value;
  var bookId=document.getElementById('bookSelect').value;
  var r=await api('/borrows/issue',{method:'POST',body:JSON.stringify({userId:parseInt(userId),bookId:parseInt(bookId)})});
  if(r.ok){showToast('Book issued successfully!');location.reload();}
  else{var d=await r.json();showToast(d.error||'Issue failed','error');}
}
async function returnBook(id){
  if(!confirm('Confirm return?'))return;
  var r=await api('/borrows/return/'+id,{method:'POST'});
  if(r.ok){var d=await r.json();var msg='Book returned.';if(d.fine>0)msg+=' Fine: ₹'+d.fine.toFixed(2);showToast(msg);loadActive();}
  else{var d=await r.json();showToast(d.error||'Return failed','error');}
}
