const API_CONFIG = {
    baseUrl: 'http://localhost:8765/api',
    endpoints: {
        tournaments: '/tournaments',
        players: '/players',
        pairings: '/pairings',
        gameResults: '/game-results'
    }
};

const ApiService = {
    get: async function(endpoint) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`);
            if (!response.ok) throw new Error(`Błąd API: ${response.status} ${response.statusText}`);
            return await response.json();
        } catch (error) {
            console.error('Błąd zapytania API:', error);
            throw error;
        }
    },

    post: async function(endpoint, data) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error(`Błąd API: ${response.status} ${response.statusText}`);
            return await response.json();
        } catch (error) {
            console.error('Błąd zapytania API:', error);
            throw error;
        }
    },

    put: async function(endpoint, data) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error(`Błąd API: ${response.status} ${response.statusText}`);
            return await response.json();
        } catch (error) {
            console.error('Błąd zapytania API:', error);
            throw error;
        }
    },

    delete: async function(endpoint) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, { method: 'DELETE' });
            if (!response.ok) throw new Error(`Błąd API: ${response.status} ${response.statusText}`);
            return true;
        } catch (error) {
            console.error('Błąd zapytania API:', error);
            throw error;
        }
    },

    // Turnieje
    getAllTournaments: function() { return this.get(API_CONFIG.endpoints.tournaments); },
    getTournamentById: function(id) { return this.get(`${API_CONFIG.endpoints.tournaments}/${id}`); },
    createTournament: function(data) { return this.post(API_CONFIG.endpoints.tournaments, data); },
    updateTournament: function(id, data) { return this.put(`${API_CONFIG.endpoints.tournaments}/${id}`, data); },
    deleteTournament: function(id) { return this.delete(`${API_CONFIG.endpoints.tournaments}/${id}`); },
    getTournamentRounds: function(id) { return this.get(`${API_CONFIG.endpoints.tournaments}/${id}/rounds`); },
    generateNextRound: function(id) { return this.post(`${API_CONFIG.endpoints.tournaments}/${id}/rounds/generate`, {}); },
    closeTournament: function(id) { return this.post(`${API_CONFIG.endpoints.tournaments}/${id}/close`, {}); },

    // Gracze
    getAllPlayers: function() { return this.get(API_CONFIG.endpoints.players); },
    getPlayerById: function(id) { return this.get(`${API_CONFIG.endpoints.players}/${id}`); },
    createPlayer: function(data) { return this.post(API_CONFIG.endpoints.players, data); },
    updatePlayer: function(id, data) { return this.put(`${API_CONFIG.endpoints.players}/${id}`, data); },
    deletePlayer: function(id) { return this.delete(`${API_CONFIG.endpoints.players}/${id}`); },
    getPlayerEloHistory: function(id) { return this.get(`${API_CONFIG.endpoints.players}/${id}/elo-history`); },

    // Pary i wyniki
    submitResult: function(pairingId, result) { return this.put(`${API_CONFIG.endpoints.pairings}/${pairingId}/result`, result); },
    getAllGameResults: function() { return this.get(API_CONFIG.endpoints.gameResults); },
};