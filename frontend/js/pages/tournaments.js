function renderTournamentsPage() {
    const appContainer = document.getElementById('app-container');
    appContainer.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="bi bi-calendar-event me-2"></i>Turnieje</h2>
            <button class="btn btn-success" id="btn-add-tournament">
                <i class="bi bi-plus-circle me-1"></i> Nowy turniej
            </button>
        </div>
        <div id="tournaments-container">
            <div class="text-center py-5">
                <div class="spinner-border" role="status"></div>
                <p class="mt-2">Ładowanie turniejów...</p>
            </div>
        </div>
    `;

    document.getElementById('btn-add-tournament').addEventListener('click', () => {
        navigateTo('tournament-create');
    });

    ApiService.getAllTournaments()
        .then(tournaments => renderTournamentsTable(tournaments))
        .catch(error => {
            document.getElementById('tournaments-container').innerHTML = `
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    Nie można połączyć się z backendem: ${error.message}
                </div>`;
        });
}

function renderTournamentsTable(tournaments) {
    const container = document.getElementById('tournaments-container');

    if (!tournaments || tournaments.length === 0) {
        container.innerHTML = `
            <div class="alert alert-info">
                <i class="bi bi-info-circle me-2"></i>
                Brak turniejów. Utwórz pierwszy turniej!
            </div>`;
        return;
    }

    const columns = [
        { field: 'id', title: 'ID', width: '5%' },
        { field: 'name', title: 'Nazwa', render: (v) => `<strong>${v}</strong>` },
        { field: 'format', title: 'Format', width: '12%' },
        { field: 'status', title: 'Status', width: '12%', render: (v) => {
            const colors = { ACTIVE: 'success', FINISHED: 'secondary', PLANNED: 'warning' };
            return `<span class="badge bg-${colors[v] || 'primary'}">${v || '-'}</span>`;
        }},
        { field: 'roundsTotal', title: 'Rundy', width: '8%' },
        { field: 'startDate', title: 'Data', width: '12%', render: (v) => v ? v.substring(0, 10) : '-' },
    ];

    const table = createTable(tournaments, {
        columns,
        onView: (id) => navigateTo('tournament-details', { id }),
        onEdit: (id) => navigateTo('tournament-edit', { id }),
        onDelete: (id, item) => {
            confirmAction(`Usunąć turniej "${item.name}"?`).then(confirmed => {
                if (!confirmed) return;
                ApiService.deleteTournament(id)
                    .then(() => { showSuccess('Turniej usunięty'); renderTournamentsPage(); })
                    .catch(e => showError(e.message));
            });
        }
    });

    container.innerHTML = '';
    container.appendChild(table);
}

function renderTournamentFormPage(id) {
    const appContainer = document.getElementById('app-container');
    const isEdit = !!id;

    appContainer.innerHTML = `
        <div class="d-flex align-items-center mb-4">
            <button class="btn btn-outline-secondary me-3" id="btn-back">
                <i class="bi bi-arrow-left"></i> Wróć
            </button>
            <h2>${isEdit ? 'Edytuj turniej' : 'Nowy turniej'}</h2>
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

    document.getElementById('btn-back').addEventListener('click', () => navigateTo('tournaments'));

    const fields = [
        { id: 'name', name: 'name', label: 'Nazwa turnieju', type: 'text', required: true, placeholder: 'np. Turniej Wiosenny 2024' },
        { id: 'format', name: 'format', label: 'Format', type: 'select', required: true,
          options: [{ value: 'SWISS', label: 'System szwajcarski' }, { value: 'ROUND_ROBIN', label: 'Każdy z każdym' }] },
        { id: 'roundsTotal', name: 'roundsTotal', label: 'Liczba rund', type: 'number', required: true,
          attributes: { min: '1', max: '20' } },
        { id: 'timeControl', name: 'timeControl', label: 'Kontrola czasu', type: 'text', placeholder: 'np. 90+30' },
        { id: 'startDate', name: 'startDate', label: 'Data rozpoczęcia', type: 'date' },
    ];

    const renderForm = (initialValues) => {
        const form = createForm(fields, {
            initialValues,
            submitLabel: isEdit ? 'Zapisz zmiany' : 'Utwórz turniej',
            onSubmit: (data) => {
                data.roundsTotal = parseInt(data.roundsTotal);
                const action = isEdit
                    ? ApiService.updateTournament(id, data)
                    : ApiService.createTournament(data);
                action
                    .then(() => { showSuccess(isEdit ? 'Turniej zaktualizowany!' : 'Turniej utworzony!'); navigateTo('tournaments'); })
                    .catch(e => showError(e.message));
            }
        });
        document.getElementById('form-container').innerHTML = '';
        document.getElementById('form-container').appendChild(form);
    };

    if (isEdit) {
        ApiService.getTournamentById(id)
            .then(t => renderForm(t))
            .catch(e => showError(e.message));
    } else {
        renderForm(null);
    }
}
