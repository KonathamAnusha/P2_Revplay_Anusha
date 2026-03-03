// ====================== BROWSE PAGE JS ======================

// Add to favorites
function addFavorite(songId) {
    if (typeof loggedUserId === 'undefined') return;
    fetch('/api/favorites?userId=' + loggedUserId + '&songId=' + songId, {
        method: 'POST'
    }).then(r => {
        if (r.ok) alert('Added to favorites!');
        else r.json().then(err => alert(err.message || 'Already in favorites'));
    }).catch(e => console.log('Error:', e));
}

// Add to playlist (prompt for playlist ID)
function addToPlaylist(songId) {
    let playlistId = prompt('Enter playlist ID:');
    if (playlistId) {
        fetch('/api/playlist-songs?playlistId=' + playlistId + '&songId=' + songId, {
            method: 'POST'
        }).then(r => {
            if (r.ok) alert('Added to playlist!');
            else alert('Failed to add');
        });
    }
}
