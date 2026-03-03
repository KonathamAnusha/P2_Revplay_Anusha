document.addEventListener("DOMContentLoaded", function () {

    const playButtons = document.querySelectorAll(".play-btn");

    playButtons.forEach(button => {
        button.addEventListener("click", function () {

            const songTitle = this.closest(".song-card")
                                   .querySelector("h5")
                                   .innerText;

            alert("Now Playing: " + songTitle);

        });
    });

});