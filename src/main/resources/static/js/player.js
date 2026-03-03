// ====================== MUSIC PLAYER JS ======================
// Use the hidden audio element from the fragment
let audio = document.getElementById('main-audio');
if (!audio) {
    audio = new Audio(); // Fallback if fragment missing
}
let isPlaying = false;
let isShuffle = false;
let repeatMode = 0; // 0=off, 1=repeat-all, 2=repeat-one
let playlist = [];
let currentIndex = -1;
let originalPlaylist = [];

// Format time mm:ss
function formatTime(seconds) {
    if (isNaN(seconds) || seconds === Infinity) return "0:00";
    let m = Math.floor(seconds / 60);
    let s = Math.floor(seconds % 60);
    return m + ':' + (s < 10 ? '0' : '') + s;
}

// Build queue from DOM elements
function buildQueueFromPage() {
    playlist = [];
    originalPlaylist = [];
    const playButtons = document.querySelectorAll('.js-play-btn');

    playButtons.forEach(btn => {
        const songId = btn.getAttribute('data-songid');
        if (songId) {
            playlist.push({
                songId: songId,
                title: btn.getAttribute('data-title'),
                artist: btn.getAttribute('data-artist'),
                audioUrl: btn.getAttribute('data-url'),
                coverImage: btn.getAttribute('data-cover') || '/images/image1.png'
            });
        }
    });
    originalPlaylist = [...playlist];
}

// Update the player UI
function updatePlayerUI(title, artist, coverImage) {
    const playerEl = document.getElementById('music-player');
    if (playerEl) playerEl.style.display = 'block';

    const titleEl = document.getElementById('player-title');
    const artistEl = document.getElementById('player-artist');
    const coverEl = document.getElementById('player-cover');

    if (titleEl) titleEl.textContent = title || 'Unknown';
    if (artistEl) artistEl.textContent = artist || 'Unknown';
    if (coverEl) coverEl.src = coverImage || '/images/image1.png';
}

// Play a specific song by ID (useful for simple track lists)
function playSongById(songId) {
    fetch('/api/songs/' + songId)
        .then(res => res.json())
        .then(song => {
            playSong(song.songId, song.title, song.artistName || 'Artist', song.songUrl, song.coverArt);
        })
        .catch(err => console.error('Error fetching song info:', err));
}

// Play a specific song
function playSong(songId, title, artist, audioUrl, coverImage) {
    // 1. Stop current audio immediately to prevent overlap
    if (audio) {
        audio.pause();
        audio.src = '';
        audio.load();
    } else {
        audio = document.getElementById('main-audio') || new Audio();
    }

    // 2. Manage Queue
    if (playlist.length === 0) {
        playlist = [{ songId, title, artist, audioUrl, coverImage }];
        originalPlaylist = [...playlist];
        currentIndex = 0;
    } else {
        let idx = playlist.findIndex(s => s.songId == songId);
        if (idx !== -1) {
            currentIndex = idx;
        } else {
            // Push to current queue and play
            playlist.push({ songId, title, artist, audioUrl, coverImage });
            currentIndex = playlist.length - 1;
        }
    }

    // 3. Update UI
    updatePlayerUI(title, artist, coverImage);

    // 4. Start Playback
    if (audioUrl && audioUrl !== 'null' && audioUrl !== '') {
        audio.src = audioUrl;
        audio.play().then(() => {
            isPlaying = true;
            const playBtn = document.getElementById('btn-play');
            if (playBtn) playBtn.textContent = '⏸️';
        }).catch(e => {
            console.warn('Playback error:', e);
            // alert("Error playing audio. The file might be missing or unreadable.");
        });
    } else {
        isPlaying = false;
        const playBtn = document.getElementById('btn-play');
        if (playBtn) playBtn.textContent = '▶️';
        alert("This track has no audio file.");
    }

    // 5. Analytics
    if (typeof loggedUserId !== 'undefined' && loggedUserId) {
        fetch('/api/history', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: loggedUserId, songId: songId, actionType: 'PLAY', duration: 0 })
        }).catch(e => { });
        fetch('/api/songs/' + songId + '/play', { method: 'PUT' }).catch(e => { });
    }
}

// Toggle play/pause
function togglePlay() {
    if (audio.src === "") return;

    if (isPlaying) {
        audio.pause();
        isPlaying = false;
        document.getElementById('btn-play').textContent = '▶️';
    } else {
        audio.play().catch(e => console.log(e));
        isPlaying = true;
        document.getElementById('btn-play').textContent = '⏸️';
    }
}

// Previous song
function prevSong() {
    if (playlist.length === 0) return;

    if (audio.currentTime > 3) {
        // Restart current song
        audio.currentTime = 0;
    } else {
        // Go to previous
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = playlist.length - 1; // loop to back if repeat all
        }
        let s = playlist[currentIndex];
        playSong(s.songId, s.title, s.artist, s.audioUrl, s.coverImage);
    }
}

// Next song
function nextSong() {
    if (playlist.length === 0) return;

    currentIndex++;
    if (currentIndex >= playlist.length) {
        if (repeatMode === 1) { // repeat all
            currentIndex = 0;
        } else {
            // End of queue
            currentIndex--;
            audio.pause();
            isPlaying = false;
            document.getElementById('btn-play').textContent = '▶️';
            return;
        }
    }

    let s = playlist[currentIndex];
    playSong(s.songId, s.title, s.artist, s.audioUrl, s.coverImage);
}

// Toggle shuffle
function toggleShuffle() {
    isShuffle = !isShuffle;
    let btn = document.getElementById('btn-shuffle');

    if (isShuffle) {
        btn.classList.replace('btn-outline-light', 'btn-light');
        btn.style.color = '#1db954';

        // Shuffle playlist but keep current song first
        if (currentIndex !== -1 && playlist.length > 0) {
            let current = playlist[currentIndex];
            let remaining = playlist.filter((_, i) => i !== currentIndex);
            remaining.sort(() => Math.random() - 0.5);
            playlist = [current, ...remaining];
            currentIndex = 0;
        }
    } else {
        btn.classList.replace('btn-light', 'btn-outline-light');
        btn.style.color = '#fff';

        // Restore original order
        if (currentIndex !== -1 && playlist.length > 0) {
            let currentId = playlist[currentIndex].songId;
            playlist = [...originalPlaylist];
            currentIndex = playlist.findIndex(s => s.songId === currentId);
        }
    }
}

// Toggle repeat
function toggleRepeat() {
    repeatMode = (repeatMode + 1) % 3;
    let btn = document.getElementById('btn-repeat');

    if (repeatMode === 0) {
        btn.textContent = '🔁';
        btn.classList.replace('btn-light', 'btn-outline-light');
        btn.style.color = '#fff';
    } else if (repeatMode === 1) {
        btn.textContent = '🔁';
        btn.classList.replace('btn-outline-light', 'btn-light');
        btn.style.color = '#1db954';
    } else {
        btn.textContent = '🔂';
        btn.classList.replace('btn-outline-light', 'btn-light');
        btn.style.color = '#1db954';
    }
}

// Seek bar update
audio.addEventListener('timeupdate', function () {
    if (audio.duration) {
        let pct = (audio.currentTime / audio.duration) * 100;
        document.getElementById('seek-bar').value = pct;
        document.getElementById('current-time').textContent = formatTime(audio.currentTime);
        document.getElementById('total-time').textContent = formatTime(audio.duration);
    }
});

// Seek bar input
document.getElementById('seek-bar').addEventListener('input', function (e) {
    if (audio.duration) {
        audio.currentTime = (e.target.value / 100) * audio.duration;
    }
});

// Volume
document.getElementById('volume-bar').addEventListener('input', function (e) {
    audio.volume = e.target.value / 100;
});

// Song ended
audio.addEventListener('ended', function () {
    if (repeatMode === 2) {
        // Repeat one
        audio.currentTime = 0;
        audio.play();
    } else {
        nextSong();
    }
});

// Queue helper function for setting global queue
function setQueue(songsArray) {
    playlist = songsArray;
    originalPlaylist = [...songsArray];
}
