function renderPlayersPage() {
    const appContainer = document.getElementById('app-container');
    appContainer.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="bi bi-people me-2"></i>Gracze</h2>
            <button class="btn btn-success" id="btn-add-player">
                <i class="bi bi-person-plus me-1"></i> Nowy gracz
            </button>
        </div>
        <div id="players-container">
            <div class="text-center py-5">
                <div class="spinner-border" role="status"></div>
                <p class="mt-2">Ładowanie graczy...</p>
            </div>
        </div>
    `;

    document.getElementById('btn-add-player').addEventListener('click', () => {
        navigateTo('player-create');
    });

    ApiService.getAllPlayers()
        .then(players => renderPlayersTable(players))
        .catch(error => {
            document.getElementById('players-container').innerHTML = `
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    Nie można połączyć się z backendem: ${error.message}
                </div>`;
        });
}

function renderPlayersTable(players) {
    const container = document.getElementById('players-container');

    if (!players || players.length === 0) {
        container.innerHTML = `
            <div class="alert alert-info">
                <i class="bi bi-info-circle me-2"></i>
                Brak graczy. Dodaj pierwszego gracza!
            </div>`;
        return;
    }

    const columns = [
        { field: 'id', title: 'ID', width: '5%' },
        { field: 'firstName', title: 'Imię' },
        { field: 'lastName', title: 'Nazwisko', render: (v) => `<strong>${v}</strong>` },
        { field: 'eloRating', title: 'ELO', width: '8%', render: (v) => `<span class="badge bg-primary">${v || '-'}</span>` },
        { field: 'fideTitle', title: 'Tytuł FIDE', width: '10%', render: (v) => v ? `<span class="badge bg-warning text-dark">${v}</span>` : '-' },
        { field: 'federation', title: 'Federacja', width: '10%' },
        { field: 'email', title: 'Email', width: '20%' },
    ];

    const table = createTable(players, {
        columns,
        onView: (id) => navigateTo('player-details', { id }),
        onEdit: (id) => navigateTo('player-edit', { id }),
        onDelete: (id, item) => {
            confirmAction(`Usunąć gracza "${item.firstName} ${item.lastName}"?`).then(confirmed => {
                if (!confirmed) return;
                ApiService.deletePlayer(id)
                    .then(() => { showSuccess('Gracz usunięty'); renderPlayersPage(); })
                    .catch(e => showError(e.message));
            });
        }
    });

    container.innerHTML = '';
    container.appendChild(table);
}

function renderPlayerFormPage(id) {
    const appContainer = document.getElementById('app-container');
    const isEdit = !!id;

    appContainer.innerHTML = `
        <div class="d-flex align-items-center mb-4">
            <button class="btn btn-outline-secondary me-3" id="btn-back">
                <i class="bi bi-arrow-left"></i> Wróć
            </button>
            <h2>${isEdit ? 'Edytuj gracza' : 'Nowy gracz'}</h2>
        </div>
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-body" id="form-container">
                        <div class="text-center py-3"><div class="spinner-border" role="status"></div></div>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.getElementById('btn-back').addEventListener('click', () => navigateTo('players'));

    const fields = [
        { id: 'firstName', name: 'firstName', label: 'Imię', type: 'text', required: true },
        { id: 'lastName', name: 'lastName', label: 'Nazwisko', type: 'text', required: true },
        { id: 'email', name: 'email', label: 'Email', type: 'text', placeholder: 'gracz@example.com' },
        { id: 'eloRating', name: 'eloRating', label: 'Ranking ELO', type: 'number', attributes: { min: '100', max: '3000' } },
        { id: 'fideTitle', name: 'fideTitle', label: 'Tytuł FIDE', type: 'select',
          options: [
              { value: '', label: 'Brak' },
              { value: 'GM', label: 'GM – Arcymistrz' },
              { value: 'IM', label: 'IM – Mistrz Międzynarodowy' },
              { value: 'FM', label: 'FM – Mistrz FIDE' },
              { value: 'CM', label: 'CM – Kandydat na Mistrza' },
              { value: 'WGM', label: 'WGM' },
              { value: 'WIM', label: 'WIM' },
          ]},
        { id: 'federation', name: 'federation', label: 'Federacja', type: 'text', placeholder: 'np. POL' },
    ];

    const renderForm = (initialValues) => {
        const form = createForm(fields, {
            initialValues,
            submitLabel: isEdit ? 'Zapisz zmiany' : 'Dodaj gracza',
            onSubmit: (data) => {
                if (data.eloRating) data.eloRating = parseInt(data.eloRating);
                const action = isEdit
                    ? ApiService.updatePlayer(id, data)
                    : ApiService.createPlayer(data);
                action
                    .then(() => { showSuccess(isEdit ? 'Dane gracza zaktualizowane!' : 'Gracz dodany!'); navigateTo('players'); })
                    .catch(e => showError(e.message));
            }
        });
        document.getElementById('form-container').innerHTML = '';
        document.getElementById('form-container').appendChild(form);
    };

    if (isEdit) {
        ApiService.getPlayerById(id)
            .then(p => renderForm(p))
            .catch(e => showError(e.message));
    } else {
        renderForm(null);
    }
}
