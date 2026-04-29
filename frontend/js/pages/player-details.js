function renderPlayerDetailsPage(id) {
    const appContainer = document.getElementById('app-container');
    appContainer.innerHTML = `
        <div class="d-flex align-items-center mb-4">
            <button class="btn btn-outline-secondary me-3" id="btn-back">
                <i class="bi bi-arrow-left"></i> Wróć
            </button>
            <h2 id="player-title">Ładowanie...</h2>
        </div>
        <div id="player-details-container">
            <div class="text-center py-5">
                <div class="spinner-border" role="status"></div>
            </div>
        </div>
    `;

    document.getElementById('btn-back').addEventListener('click', () => navigateTo('players'));

    ApiService.getPlayerById(id)
        .then(player => {
            document.getElementById('player-title').textContent =
                `${player.firstName} ${player.lastName}`;
            renderPlayerDetails(player);
        })
        .catch(e => {
            document.getElementById('player-details-container').innerHTML =
                `<div class="alert alert-danger">${e.message}</div>`;
        });
}

function renderPlayerDetails(p) {
    const container = document.getElementById('player-details-container');

    container.innerHTML = `
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card h-100">
                    <div class="card-header bg-primary text-white"><h5 class="mb-0">Profil gracza</h5></div>
                    <div class="card-body">
                        <div class="text-center mb-3">
                            <div class="display-1 text-muted"><i class="bi bi-person-circle"></i></div>
                            <h4>${p.firstName} ${p.lastName}</h4>
                            ${p.fideTitle ? `<span class="badge bg-warning text-dark fs-6">${p.fideTitle}</span>` : ''}
                        </div>
                        <table class="table table-borderless mb-0">
                            <tr><th>ELO</th><td><strong class="text-primary fs-5">${p.eloRating || '-'}</strong></td></tr>
                            <tr><th>Email</th><td>${p.email || '-'}</td></tr>
                            <tr><th>Federacja</th><td>${p.federation || '-'}</td></tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="card h-100">
                    <div class="card-header bg-info text-white"><h5 class="mb-0">Historia ELO</h5></div>
                    <div class="card-body" id="elo-history-container">
                        <div class="text-center"><div class="spinner-border spinner-border-sm" role="status"></div></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex gap-2">
            <button class="btn btn-warning" onclick="navigateTo('player-edit', { id: ${p.id} })">
                <i class="bi bi-pencil me-1"></i> Edytuj
            </button>
        </div>
    `;

    ApiService.getPlayerEloHistory(p.id)
        .then(history => renderEloHistory(history))
        .catch(() => {
            document.getElementById('elo-history-container').innerHTML =
                `<p class="text-muted">Brak historii ELO.</p>`;
        });
}

function renderEloHistory(history) {
    const container = document.getElementById('elo-history-container');

    if (!history || history.length === 0) {
        container.innerHTML = `<p class="text-muted">Brak historii ELO — gracz nie brał jeszcze udziału w turniejach.</p>`;
        return;
    }

    const rows = history.map(h => `
        <tr>
            <td>${h.id}</td>
            <td>${h.ratingBefore || '-'}</td>
            <td>${h.ratingAfter || '-'}</td>
            <td>
                <span class="badge bg-${h.delta > 0 ? 'success' : h.delta < 0 ? 'danger' : 'secondary'}">
                    ${h.delta > 0 ? '+' : ''}${h.delta || 0}
                </span>
            </td>
            <td>${h.recordedAt ? h.recordedAt.substring(0, 10) : '-'}</td>
        </tr>
    `).join('');

    container.innerHTML = `
        <table class="table table-sm table-hover">
            <thead>
                <tr>
                    <th>#</th>
                    <th>ELO przed</th>
                    <th>ELO po</th>
                    <th>Zmiana</th>
                    <th>Data</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}
