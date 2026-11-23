// ── books.js ────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded',loadBooks);
document.getElementById('searchInput').addEventListener('keyup',function(e){if(e.key==='Enter')searchBooks();});

async function loadBooks(){
  var r=await api('/books');renderBooks(await r.json());
}
async function searchBooks(){
  var kw=document.getElementById('searchInput').value.trim();
  if(!kw){loadBooks();return;}
  var r=await api('/books/search?keyword='+encodeURIComponent(kw));renderBooks(await r.json());
}
function renderBooks(list){
  var tb=document.getElementById('booksTable');tb.innerHTML='';
  list.forEach(function(b){
    tb.innerHTML+='<tr><td>'+b.id+'</td><td>'+b.title+'</td><td>'+b.author+'</td><td>'+b.isbn+'</td><td>'+b.category+'</td><td>'+b.quantity+'</td><td>'+b.availableQuantity+'</td><td>'+badge(b.status)+'</td><td><button class="btn btn-sm btn-outline" onclick="editBook('+b.id+')"><i class="fas fa-edit"></i></button> <button class="btn btn-sm btn-danger" onclick="deleteBook('+b.id+')"><i class="fas fa-trash"></i></button></td></tr>';
  });
  if(!list.length)tb.innerHTML='<tr><td colspan="9" style="text-align:center;color:#999">No books found</td></tr>';
}
function openModal(){ document.getElementById('bookForm').reset();document.getElementById('bookId').value='';document.getElementById('modalTitle').textContent='Add Book';document.getElementById('modal').classList.add('active');}
function closeModal(){document.getElementById('modal').classList.remove('active');}
async function editBook(id){
  var r=await api('/books/'+id);var b=await r.json();
  document.getElementById('bookId').value=b.id;
  document.getElementById('bTitle').value=b.title;
  document.getElementById('bAuthor').value=b.author;
  document.getElementById('bIsbn').value=b.isbn;
  document.getElementById('bCategory').value=b.category||'';
  document.getElementById('bQty').value=b.quantity;
  document.getElementById('modalTitle').textContent='Edit Book';
  document.getElementById('modal').classList.add('active');
}
async function saveBook(e){
  e.preventDefault();
  var id=document.getElementById('bookId').value;
  var body={title:document.getElementById('bTitle').value,author:document.getElementById('bAuthor').value,isbn:document.getElementById('bIsbn').value,category:document.getElementById('bCategory').value,quantity:parseInt(document.getElementById('bQty').value)};
  var r;
  if(id){r=await api('/books/'+id,{method:'PUT',body:JSON.stringify(body)});}
  else{r=await api('/books',{method:'POST',body:JSON.stringify(body)});}
  if(r.ok){closeModal();showToast(id?'Book updated':'Book added');loadBooks();}
  else{var d=await r.json();showToast(d.error||'Error','error');}
}
async function deleteBook(id){
  if(!confirm('Delete this book?'))return;
  var r=await api('/books/'+id,{method:'DELETE'});
  if(r.ok){showToast('Book deleted');loadBooks();}
  else{showToast('Error deleting book','error');}
}
