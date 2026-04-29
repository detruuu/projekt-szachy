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

    // Initial route render
    renderCurrentRoute();
}

// Initialize the app when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', initApp);