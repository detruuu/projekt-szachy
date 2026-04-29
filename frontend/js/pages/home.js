/**
 * Home Page Controller
 * Renders the home/welcome page of the application
 */

/**
 * Render the home page
 */
function renderHomePage() {
    const appContainer = document.getElementById('app-container');

    appContainer.innerHTML = `
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card shadow-sm mb-4">
                    <div class="card-body text-center py-5">
                        <h1 class="card-title mb-3">
                            <i class="bi bi-trophy-fill text-warning me-2"></i>
                            System Turniejów Szachowych
                        </h1>
                        <p class="card-text text-muted fs-5">
                            Zarządzaj turniejami w systemie szwajcarskim, parami i rankingiem ELO graczy.
                        </p>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-4">
                        <div class="card card-hover h-100">
                            <div class="card-body text-center py-4">
                                <i class="bi bi-calendar-event display-4 text-primary mb-3"></i>
                                <h4>Turnieje</h4>
                                <p class="text-muted">Przeglądaj i zarządzaj turniejami szachowymi</p>
                                <button class="btn btn-primary" data-action="go-tournaments">
                                    <i class="bi bi-arrow-right-circle me-1"></i> Przejdź do turniejów
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-4">
                        <div class="card card-hover h-100">
                            <div class="card-body text-center py-4">
                                <i class="bi bi-people display-4 text-success mb-3"></i>
                                <h4>Gracze</h4>
                                <p class="text-muted">Przeglądaj profile graczy i historię ELO</p>
                                <button class="btn btn-success" data-action="go-players">
                                    <i class="bi bi-arrow-right-circle me-1"></i> Przejdź do graczy
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.querySelector('[data-action="go-tournaments"]').addEventListener('click', () => {
        navigateTo('tournaments');
    });
    document.querySelector('[data-action="go-players"]').addEventListener('click', () => {
        navigateTo('players');
    });
}