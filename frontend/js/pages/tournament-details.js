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
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-primary text-white"><h5 class="mb-0"><i class="bi bi-info-circle me-1"></i>Informacje</h5></div>
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
                
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-dark text-white"><h5 class="mb-0"><i class="bi bi-gear-fill me-1"></i>Zarządzanie turniejem</h5></div>
                    <div class="card-body d-flex flex-column gap-2">
                        <button class="btn btn-success" id="btn-generate-round">
                            <i class="bi bi-play-circle me-1"></i> Generuj rundę
                        </button>
                        <button class="btn btn-warning text-dark" onclick="navigateTo('tournament-edit', { id: ${t.id} })">
                            <i class="bi bi-pencil me-1"></i> Edytuj turniej
                        </button>
                        <button class="btn btn-danger" id="btn-close-tournament">
                            <i class="bi bi-check-circle me-1"></i> Zamknij turniej i przelicz ELO
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-md-7">
                <div class="card shadow-sm h-100">
                    <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="bi bi-list-ol me-1"></i>Rundy i Parowania</h5>
                    </div>
                    <div class="card-body" id="rounds-container">
                        <div class="text-center"><div class="spinner-border spinner-border-sm" role="status"></div></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="pairings-view-container" class="mt-4"></div>
    `;

    document.getElementById('btn-generate-round').addEventListener('click', () => {
        ApiService.generateNextRound(t.id)
            .then(() => { showSuccess('Runda wygenerowana!'); renderTournamentDetailsPage(t.id); })
            .catch(e => showError('Błąd generowania rundy: ' + e.message));
    });

    document.getElementById('btn-close-tournament').addEventListener('click', () => {
        confirmAction('Czy na pewno chcesz zamknąć ten turniej i przeliczyć rankingi ELO zawodników?').then(confirmed => {
            if (!confirmed) return;
            ApiService.closeTournament(t.id)
                .then(() => { showSuccess('Turniej zamknięty, rankingi ELO zostały zaktualizowane!'); renderTournamentDetailsPage(t.id); })
                .catch(e => showError('Błąd podczas zamykania turnieju: ' + e.message));
        });
    });

    ApiService.getTournamentRounds(t.id)
        .then(rounds => renderRounds(t.id, rounds))
        .catch(() => {
            document.getElementById('rounds-container').innerHTML =
                `<p class="text-muted">Brak rund lub błąd ładowania.</p>`;
        });
}

function renderRounds(tournamentId, rounds) {
    const container = document.getElementById('rounds-container');
    if (!rounds || rounds.length === 0) {
        container.innerHTML = `<p class="text-muted">Brak rund. Wygeneruj pierwszą rundę!</p>`;
        return;
    }

    const html = rounds.map(r => `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2 round-item-row" data-round="${r.roundNumber}" style="cursor: pointer;">
            <span><strong>Runda ${r.roundNumber}</strong></span>
            <div>
                <span class="badge bg-${r.status === 'COMPLETED' ? 'success' : 'warning'} me-2">${r.status || '-'}</span>
                <button class="btn btn-sm btn-outline-primary btn-view-pairings" data-round="${r.roundNumber}">Pokaż kojarzenia</button>
            </div>
        </div>
    `).join('');

    container.innerHTML = `<div>${html}</div>`;

    document.querySelectorAll('.round-item-row').forEach(row => {
        row.addEventListener('click', () => {
            const roundNumber = row.getAttribute('data-round');
            loadAndRenderPairings(tournamentId, parseInt(roundNumber));
        });
    });

    // Auto-load pairings for the last round by default
    const lastRound = rounds[rounds.length - 1];
    if (lastRound) {
        loadAndRenderPairings(tournamentId, lastRound.roundNumber);
    }
}

function loadAndRenderPairings(tournamentId, roundNumber) {
    const viewContainer = document.getElementById('pairings-view-container');
    viewContainer.innerHTML = `
        <div class="card shadow-sm">
            <div class="card-body text-center py-4">
                <div class="spinner-border" role="status"></div>
                <p class="mt-2">Ładowanie skojarzeń dla rundy ${roundNumber}...</p>
            </div>
        </div>
    `;

    Promise.all([
        ApiService.get(`/tournaments/${tournamentId}/rounds/${roundNumber}`),
        ApiService.getAllGameResults()
    ])
    .then(([pairings, results]) => {
        const resultsMap = {};
        if (results && Array.isArray(results)) {
            results.forEach(res => {
                if (res.pairing) {
                    resultsMap[res.pairing.id] = {
                        result: res.result,
                        forfeit: res.forfeit
                    };
                }
            });
        } else if (results && typeof results === 'object') {
            Object.values(results).forEach(res => {
                if (res && res.pairing) {
                    resultsMap[res.pairing.id] = { result: res.result, forfeit: res.forfeit };
                }
            });
        }
        renderPairingsTable(tournamentId, roundNumber, pairings, resultsMap);
    })
    .catch(e => {
        viewContainer.innerHTML = `
            <div class="alert alert-danger">
                Nie udało się załadować parowań dla rundy ${roundNumber}: ${e.message}
            </div>
        `;
    });
}

function renderPairingsTable(tournamentId, roundNumber, pairings, resultsMap) {
    const viewContainer = document.getElementById('pairings-view-container');
    if (!pairings || pairings.length === 0) {
        viewContainer.innerHTML = `
            <div class="card shadow-sm">
                <div class="card-header bg-secondary text-white"><h5>Runda ${roundNumber} — Kojarzenia</h5></div>
                <div class="card-body">
                    <p class="text-muted">Brak parowań dla tej rundy.</p>
                </div>
            </div>
        `;
        return;
    }

    const isLoggedIn = !!sessionStorage.getItem('auth_username');

    const rows = pairings.map(p => {
        const whiteName = p.playerWhite ? `${p.playerWhite.firstName} ${p.playerWhite.lastName} (${p.playerWhite.eloRating || 1000})` : 'BYE';
        const blackName = p.playerBlack ? `${p.playerBlack.firstName} ${p.playerBlack.lastName} (${p.playerBlack.eloRating || 1000})` : 'BYE';
        const resultInfo = resultsMap[p.id];
        let resultText = '<span class="badge bg-secondary">Brak</span>';
        if (resultInfo) {
            resultText = `<strong class="fs-5">${resultInfo.result}</strong> ${resultInfo.forfeit ? '<span class="badge bg-danger">Walkower</span>' : ''}`;
        }

        const actionBtn = isLoggedIn ? `
            <button class="btn btn-sm btn-primary btn-enter-result" data-pairing-id="${p.id}" data-white="${p.playerWhite ? p.playerWhite.lastName : ''}" data-black="${p.playerBlack ? p.playerBlack.lastName : ''}">
                <i class="bi bi-pencil-square"></i> Wynik
            </button>
        ` : '';

        return `
            <tr>
                <td class="text-center"><strong>${p.boardNumber}</strong></td>
                <td>${whiteName}</td>
                <td class="text-center">${resultText}</td>
                <td>${blackName}</td>
                <td class="text-center">${actionBtn}</td>
            </tr>
        `;
    }).join('');

    viewContainer.innerHTML = `
        <div class="card shadow-sm mb-5">
            <div class="card-header bg-secondary text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="bi bi-grid-3x3-gap me-1"></i>Runda ${roundNumber} — Kojarzenia</h5>
                ${isLoggedIn ? '<span class="badge bg-light text-dark">Tryb Arbitra</span>' : ''}
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 10%;" class="text-center">Szachownica</th>
                                <th style="width: 35%;">Białe (W)</th>
                                <th style="width: 15%;" class="text-center">Wynik</th>
                                <th style="width: 35%;">Czarne (B)</th>
                                <th style="width: 10%;" class="text-center">Akcja</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;

    document.querySelectorAll('.btn-enter-result').forEach(btn => {
        btn.addEventListener('click', () => {
            const pairingId = btn.getAttribute('data-pairing-id');
            const white = btn.getAttribute('data-white');
            const black = btn.getAttribute('data-black');
            promptAndSubmitResult(tournamentId, roundNumber, pairingId, white, black);
        });
    });
}

function promptAndSubmitResult(tournamentId, roundNumber, pairingId, white, black) {
    Swal.fire({
        title: 'Wprowadź wynik partii',
        text: `${white} vs ${black}`,
        input: 'select',
        inputOptions: {
            '1-0': '1 - 0 (Wygrana białych)',
            '0-1': '0 - 1 (Wygrana czarnych)',
            '½-½': '½ - ½ (Remis)',
            '1-0_forfeit': '1 - 0 (Walkower dla białych)',
            '0-1_forfeit': '0 - 1 (Walkower dla czarnych)'
        },
        inputPlaceholder: 'Wybierz wynik...',
        showCancelButton: true,
        confirmButtonText: 'Zapisz',
        cancelButtonText: 'Anuluj',
        inputValidator: (value) => {
            if (!value) {
                return 'Musisz wybrać wynik!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            let selectedResult = result.value;
            let forfeit = false;
            if (selectedResult.endsWith('_forfeit')) {
                selectedResult = selectedResult.replace('_forfeit', '');
                forfeit = true;
            }

            ApiService.submitResult(pairingId, { result: selectedResult, forfeit: forfeit })
                .then(() => {
                    showSuccess('Wynik został zapisany pomyślnie!');
                    loadAndRenderPairings(tournamentId, roundNumber);
                })
                .catch(e => {
                    showError('Błąd zapisu wyniku: ' + e.message);
                });
        }
    });
}
