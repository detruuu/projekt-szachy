/**
 * Main application script
 * Handles routing and application initialization
 */

// Application state
const appState = {
    currentRoute: 'home',
    params: {},
};

// DOM elements
const appContainer = document.getElementById('app-container');

/**
 * Simple router implementation
 * @param {string} route - Route name to navigate to
 * @param {Object} params - Route parameters
 */
function navigateTo(route, params = {}) {
    appState.currentRoute = route;
    appState.params = params;
    renderCurrentRoute();

    // Update active nav link
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-route') === route) {
            link.classList.add('active');
        }
    });
}

/**
 * Renders the current route content
 */
function renderCurrentRoute() {
    // Show loading indicator
    appContainer.innerHTML = `
        <div class="text-center">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `;

    // Render content based on route
    switch (appState.currentRoute) {
        case 'home':
            renderHomePage();
            break;
        case 'tournaments':
            renderTournamentsPage();
            break;
        case 'tournament-details':
            renderTournamentDetailsPage(appState.params.id);
            break;
        case 'tournament-create':
            renderTournamentFormPage();
            break;
        case 'tournament-edit':
            renderTournamentFormPage(appState.params.id);
            break;
        case 'players':
            renderPlayersPage();
            break;
        case 'player-details':
            renderPlayerDetailsPage(appState.params.id);
            break;
        case 'player-create':
            renderPlayerFormPage();
            break;
        case 'player-edit':
            renderPlayerFormPage(appState.params.id);
            break;
        default:
            appContainer.innerHTML = `<div class="alert alert-warning">Strona nie została znaleziona.</div>`;
    }
}

/**
 * Shows an error message
 * @param {string} message - Error message to display
 */
function showError(message) {
    Swal.fire({ title: 'Błąd!', text: message, icon: 'error', confirmButtonText: 'OK' });
}

function showSuccess(message) {
    Swal.fire({ title: 'Sukces!', text: message, icon: 'success', confirmButtonText: 'OK' });
}

function confirmAction(message) {
    return Swal.fire({
        title: 'Czy jesteś pewny?',
        text: message,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Tak',
        cancelButtonText: 'Nie'
    }).then(result => result.isConfirmed);
}

function renderAuthSection() {
    const section = document.getElementById('auth-nav-section');
    if (!section) return;

    const username = sessionStorage.getItem('auth_username');
    if (username) {
        section.innerHTML = `
            <span class="text-white me-3"><i class="bi bi-person-fill"></i> ${username}</span>
            <button class="btn btn-outline-light btn-sm" id="btn-logout">Wyloguj</button>
        `;
        document.getElementById('btn-logout').addEventListener('click', () => {
            sessionStorage.removeItem('auth_username');
            sessionStorage.removeItem('auth_password');
            renderAuthSection();
            showSuccess('Wylogowano pomyślnie');
            renderCurrentRoute();
        });
    } else {
        section.innerHTML = `
            <button class="btn btn-outline-light btn-sm" id="btn-login-prompt">Zaloguj się</button>
        `;
        document.getElementById('btn-login-prompt').addEventListener('click', () => {
            Swal.fire({
                title: 'Logowanie',
                html: `
                    <input type="text" id="swal-username" class="swal2-input" placeholder="Użytkownik (np. arbiter, admin)">
                    <input type="password" id="swal-password" class="swal2-input" placeholder="Hasło">
                `,
                confirmButtonText: 'Zaloguj',
                focusConfirm: false,
                preConfirm: () => {
                    const usernameInput = Swal.getPopup().querySelector('#swal-username').value;
                    const passwordInput = Swal.getPopup().querySelector('#swal-password').value;
                    if (!usernameInput || !passwordInput) {
                        Swal.showValidationMessage(`Wprowadź login i hasło`);
                    }
                    return { username: usernameInput, password: passwordInput };
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    sessionStorage.setItem('auth_username', result.value.username);
                    sessionStorage.setItem('auth_password', result.value.password);
                    renderAuthSection();
                    showSuccess('Zalogowano pomyślnie jako ' + result.value.username);
                    renderCurrentRoute();
                }
            });
        });
    }
}

/**
 * Initialize the application
 */
function initApp() {
    // Set up navigation event listeners
    document.querySelectorAll('[data-route]').forEach(element => {
        element.addEventListener('click', (e) => {
            e.preventDefault();
            const route = e.target.getAttribute('data-route');
            navigateTo(route);
        });
    });

    renderAuthSection();

    // Initial route render
    renderCurrentRoute();
}

// Initialize the app when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', initApp);
