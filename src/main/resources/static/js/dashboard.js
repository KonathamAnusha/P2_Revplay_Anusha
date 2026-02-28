// dashboard.js

// Dummy song list for demo
let songList = ["Song A", "Song B", "Song C"];
let currentIndex = 0;
let isPlaying = false;

// Elements
let currentSong = document.getElementById('currentSong');
let playButton = document.querySelector('.player-bar button:nth-child(2)');

// Update current song display
function updateSong() {
    currentSong.textContent = songList[currentIndex] || "No song selected";
}
updateSong();

// Play/pause toggle
playButton.addEventListener('click', () => {
    isPlaying = !isPlaying;
    playButton.textContent = isPlaying ? "⏸" : "▶";
});

// Next/Previous
document.querySelector('.player-bar button:nth-child(1)').addEventListener('click', () => {
    currentIndex = (currentIndex - 1 + songList.length) % songList.length;
    updateSong();
});

document.querySelector('.player-bar button:nth-child(3)').addEventListener('click', () => {
    currentIndex = (currentIndex + 1) % songList.length;
    updateSong();
});

// Slider (volume or progress)
document.querySelector('.player-bar input[type="range"]').addEventListener('input', (e) => {
    console.log("Slider value: ", e.target.value);
});