document.addEventListener("DOMContentLoaded", function () {
    const roleSelect = document.getElementById("roleSelect");
    const artistFields = document.getElementById("artistFields");

    function toggleArtistFields() {
        artistFields.style.display = roleSelect.value === "ARTIST" ? "block" : "none";
    }

    roleSelect.addEventListener("change", toggleArtistFields);
    toggleArtistFields(); // initial check
});