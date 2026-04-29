function renderTournamentDetailsPage(id) {
    const appContainer = document.getElementById('app-container');
    appContainer.innerHTML = `
        <div class="d-flex align-items-center mb-4">
            <button class="btn btn-outline-secondary me-3" id="btn-back">
                <i class="bi bi-arrow-left"></i> Wróć
            </button>
            <h2 id="tournament-title">Ładowanie...</h2>
        </div>
        <div id="tournament-details-container">
            <div class="text-center py-5">
                <div class="spinner-border" role="status"></div>
            </div>
        </div>
    `;

    document.getElementById('btn-back').addEventListener('click', () => navigateTo('tournaments'));

    ApiService.getTournamentById(id)
        .then(tournament => {
            document.getElementById('tournament-title').textContent = tournament.name;
            renderTournamentDetails(tournament);
        })
        .catch(e => {
            document.getElementById('tournament-details-container').innerHTML =
                `<div class="alert alert-danger">${e.message}</div>`;
        });
}

function renderTournamentDetails(t) {
    const container = document.getElementById('tournament-details-container');
    const statusColors = { ACTIVE: 'success', FINISHED: 'secondary', PLANNED: 'warning' };

    container.innerHTML = `
        <div class="row mb-4">
            <div class="col-md-5">
                <div class="card h-100">
                    <div class="card-header bg-primary text-white"><h5 class="mb-0">Informacje</h5></div>
                    <div class="card-body">
                        <table class="table table-borderless mb-0">
                            <tr><th>Status</th><td><span class="badge bg-${statusColors[t.status] || 'primary'}">${t.status || '-'}</span></td></tr>
                            <tr><th>Format</th><td>${t.format || '-'}</td></tr>
                            <tr><th>Rundy</th><td>${t.roundsTotal || '-'}</td></tr>
                            <tr><th>Kontrola czasu</th><td>${t.timeControl || '-'}</td></tr>
                            <tr><th>Data</th><td>${t.startDate ? t.startDate.substring(0, 10) : '-'}</td></tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-7">
                <div class="card h-100">
                    <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Rundy</h5>
                        <button class="btn btn-sm btn-light" id="btn-generate-round">
                            <i class="bi bi-play-circle me-1"></i> Generuj rundę
                        </button>
                    </div>
                    <div class="card-body" id="rounds-container">
                        <div class="text-center"><div class="spinner-border spinner-border-sm" role="status"></div></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex gap-2">
            <button class="btn btn-warning" onclick="navigateTo('tournament-edit', { id: ${t.id} })">
                <i class="bi bi-pencil me-1"></i> Edytuj
            </button>
        </div>
    `;

    document.getElementById('btn-generate-round').addEventListener('click', () => {
        ApiService.generateNextRound(t.id)
            .then(() => { showSuccess('Runda wygenerowana!'); renderTournamentDetailsPage(t.id); })
            .catch(e => showError('Błąd generowania rundy: ' + e.message));
    });

    ApiService.getTournamentRounds(t.id)
        .then(rounds => renderRounds(rounds))
        .catch(() => {
            document.getElementById('rounds-container').innerHTML =
                `<p class="text-muted">Brak rund lub błąd ładowania.</p>`;
        });
}

function renderRounds(rounds) {
    const container = document.getElementById('rounds-container');
    if (!rounds || rounds.length === 0) {
        container.innerHTML = `<p class="text-muted">Brak rund. Wygeneruj pierwszą rundę!</p>`;
        return;
    }

    const html = rounds.map(r => `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2">
            <span><strong>Runda ${r.roundNumber}</strong></span>
            <span class="badge bg-${r.status === 'COMPLETED' ? 'success' : 'warning'}">${r.status || '-'}</span>
        </div>
    `).join('');

    container.innerHTML = `<div>${html}</div>`;
}
