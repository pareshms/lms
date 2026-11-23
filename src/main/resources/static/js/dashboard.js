// ── dashboard.js ────────────────────────────────────────────
// Enterprise Library Management System Dashboard Controller

document.addEventListener('DOMContentLoaded', () => {
  setupUserContext();
  loadDashboardMetrics();
  loadCirculationLedger();
});

function setupUserContext() {
  const userName = localStorage.getItem('userName') || 'Administrator';
  const role = localStorage.getItem('role') || 'Librarian';

  const nameEl = document.getElementById('userName');
  const roleEl = document.getElementById('userRole');
  const initialEl = document.getElementById('userInitial');

  if (nameEl) nameEl.textContent = userName;
  if (roleEl) roleEl.textContent = role;
  if (initialEl) initialEl.textContent = userName.charAt(0).toUpperCase();
}

async function loadDashboardMetrics() {
  try {
    const res = await api('/dashboard/stats');
    if (!res.ok) throw new Error('Failed to load operational metrics');
    const data = await res.json();

    const totalTitles = data.totalBooks || 0;
    const issuedLoans = data.issuedBooks || 0;
    const overdueLoans = data.overdueBooks || 0;
    const pendingFines = data.pendingFines || 0;
    const studentCount = data.studentsCount || 0;
    const facultyCount = data.facultyCount || 0;
    const categories = data.categoryStats || {};

    // 1. KPI Metric Cards
    document.getElementById('totalBooks').textContent = totalTitles.toLocaleString();
    document.getElementById('issuedBooks').textContent = issuedLoans.toLocaleString();
    document.getElementById('overdueBooks').textContent = overdueLoans.toLocaleString();

    // Available Copies calculation
    const estAvailable = Math.max(0, totalTitles - (issuedLoans + overdueLoans));
    document.getElementById('availCopies').textContent = estAvailable.toLocaleString();

    // Fine Metric Subtitle
    const finesEl = document.getElementById('finesSummary');
    if (finesEl) {
      if (pendingFines > 0) {
        finesEl.textContent = `₹${pendingFines.toFixed(2)} unpaid overdue fines`;
      } else {
        finesEl.textContent = 'No overdue penalties currently accrued';
        finesEl.style.color = 'var(--text-light)';
      }
    }

    // 2. Patron Registry Breakdown
    document.getElementById('countStudents').textContent = studentCount;
    document.getElementById('countFaculty').textContent = facultyCount;

    // 3. Circulation Health Segmented Progress Bar
    renderCirculationHealth(estAvailable, issuedLoans, overdueLoans);

    // 4. Category Inventory Breakdown Table
    renderCategoryTable(categories, totalTitles);

  } catch (err) {
    console.error('Error fetching dashboard statistics:', err);
  }
}

function renderCirculationHealth(available, issued, overdue) {
  const total = available + issued + overdue || 1;

  const pctAvail = ((available / total) * 100).toFixed(1);
  const pctIssued = ((issued / total) * 100).toFixed(1);
  const pctOverdue = ((overdue / total) * 100).toFixed(1);

  const barAvail = document.getElementById('barAvail');
  const barIssued = document.getElementById('barIssued');
  const barOverdue = document.getElementById('barOverdue');

  if (barAvail) barAvail.style.width = `${pctAvail}%`;
  if (barIssued) barIssued.style.width = `${pctIssued}%`;
  if (barOverdue) barOverdue.style.width = `${pctOverdue}%`;

  document.getElementById('statAvailCount').textContent = available;
  document.getElementById('statAvailShare').textContent = `${pctAvail}%`;

  document.getElementById('statIssuedCount').textContent = issued;
  document.getElementById('statIssuedShare').textContent = `${pctIssued}%`;

  document.getElementById('statOverdueCount').textContent = overdue;
  document.getElementById('statOverdueShare').textContent = `${pctOverdue}%`;

  const totalCopiesEl = document.getElementById('circTotalCopies');
  if (totalCopiesEl) {
    totalCopiesEl.textContent = `${total} Total Units Tracked`;
  }
}

function renderCategoryTable(categories, totalTitles) {
  const tbody = document.getElementById('categoryTableBody');
  const metaEl = document.getElementById('categoryCountMeta');
  if (!tbody) return;

  tbody.innerHTML = '';
  const entries = Object.entries(categories);

  if (metaEl) {
    metaEl.textContent = `${entries.length} Categories`;
  }

  if (entries.length === 0) {
    tbody.innerHTML = '<tr><td colspan="4" style="text-align:center;color:#718096;padding:16px;">No catalog categories indexed</td></tr>';
    return;
  }

  // Sort descending by title count
  entries.sort((a, b) => b[1] - a[1]);

  const maxVal = Math.max(...entries.map(e => e[1]), 1);

  entries.forEach(([catName, count]) => {
    const meterPct = ((count / maxVal) * 100).toFixed(0);
    const sharePct = totalTitles > 0 ? ((count / totalTitles) * 100).toFixed(0) : 0;

    const row = document.createElement('tr');
    row.innerHTML = `
      <td><strong>${escapeHtml(catName)}</strong></td>
      <td>
        <div class="cat-meter-track" title="${count} titles (${sharePct}%)">
          <div class="cat-meter-fill" style="width: ${meterPct}%;"></div>
        </div>
      </td>
      <td style="text-align: right; font-weight: 600;">${count}</td>
      <td style="text-align: right; color: var(--text-light);">${sharePct}%</td>
    `;
    tbody.appendChild(row);
  });
}

async function loadCirculationLedger() {
  const tbody = document.getElementById('recentActivityBody');
  if (!tbody) return;

  try {
    const res = await api('/borrows');
    if (!res.ok) throw new Error('Failed to load transaction records');
    const records = await res.json();

    tbody.innerHTML = '';

    if (!records.length) {
      tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#718096;padding:20px;">No circulation transactions recorded</td></tr>';
      return;
    }

    // Display most recent 8 transactions
    records.slice(0, 8).forEach(rec => {
      const fineFormatted = rec.fine > 0 
        ? `<span style="color:var(--danger);font-weight:600;">₹${rec.fine.toFixed(2)}</span>` 
        : '<span style="color:var(--text-light);">₹0.00</span>';

      const userTypeBadge = rec.user.userType === 'FACULTY' 
        ? '<span class="badge badge-blue">Faculty</span>' 
        : '<span class="badge badge-gray">Student</span>';

      const row = document.createElement('tr');
      row.innerHTML = `
        <td style="font-family:monospace;font-size:12px;color:var(--text-light);">#${rec.id}</td>
        <td><strong>${escapeHtml(rec.user.name)}</strong></td>
        <td>${userTypeBadge}</td>
        <td>${escapeHtml(rec.book.title)}</td>
        <td>${fmt(rec.issueDate)}</td>
        <td>${fmt(rec.dueDate)}</td>
        <td>${fineFormatted}</td>
        <td style="text-align:center;">${badge(rec.status)}</td>
      `;
      tbody.appendChild(row);
    });

  } catch (err) {
    console.error('Error loading transaction ledger:', err);
    tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:var(--danger);padding:16px;">Failed to load transactions</td></tr>';
  }
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/[&<>"']/g, m => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;'
  })[m]);
}
