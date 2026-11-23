// ── reports.js ──────────────────────────────────────────────
document.addEventListener('DOMContentLoaded',loadReports);
async function loadReports(){
  // Overdue
  var r1=await api('/borrows/overdue');var od=await r1.json();
  var t1=document.getElementById('overdueTable');t1.innerHTML='';
  od.forEach(function(b){
    var days=Math.ceil((new Date()-new Date(b.dueDate))/(1000*60*60*24));
    t1.innerHTML+='<tr><td>'+b.id+'</td><td>'+b.user.name+'</td><td>'+b.book.title+'</td><td>'+fmt(b.issueDate)+'</td><td>'+fmt(b.dueDate)+'</td><td>'+days+'</td><td>₹'+b.fine.toFixed(2)+'</td></tr>';
  });
  if(!od.length)t1.innerHTML='<tr><td colspan="7" style="text-align:center;color:#999">No overdue books 🎉</td></tr>';
  // Issued
  var r2=await api('/borrows/active');var ac=await r2.json();
  var t2=document.getElementById('issuedTable');t2.innerHTML='';
  ac.forEach(function(b){
    t2.innerHTML+='<tr><td>'+b.id+'</td><td>'+b.user.name+'</td><td>'+b.book.title+'</td><td>'+b.book.isbn+'</td><td>'+fmt(b.issueDate)+'</td><td>'+fmt(b.dueDate)+'</td><td>'+badge(b.status)+'</td><td>₹'+b.fine.toFixed(2)+'</td></tr>';
  });
  if(!ac.length)t2.innerHTML='<tr><td colspan="8" style="text-align:center;color:#999">No issued books</td></tr>';
}
function exportCSV(type){window.open('/api/reports/'+type+'/csv','_blank');}
