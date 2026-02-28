// Wait until DOM is loaded
document.addEventListener("DOMContentLoaded", function () {

    // Get the login form
    const loginForm = document.querySelector("form");

    if (!loginForm) return;

    // Listen for form submission
    loginForm.addEventListener("submit", function (e) {
        // Clear previous errors
        const errorAlert = document.querySelector(".alert-danger");
        if (errorAlert) errorAlert.style.display = "none";

        // Get email and password values
        const email = loginForm.querySelector('input[name="email"]').value.trim();
        const password = loginForm.querySelector('input[name="password"]').value.trim();

        // Basic validation
        if (email === "" || password === "") {
            e.preventDefault(); // stop form from submitting
            showError("Both email and password are required.");
            return;
        }

        if (!validateEmail(email)) {
            e.preventDefault();
            showError("Please enter a valid email address.");
            return;
        }
    });

    // Function to display error
    function showError(message) {
        let alertDiv = document.querySelector(".alert-danger");

        if (!alertDiv) {
            alertDiv = document.createElement("div");
            alertDiv.className = "alert alert-danger";
            loginForm.prepend(alertDiv);
        }

        alertDiv.textContent = message;
        alertDiv.style.display = "block";
    }

    // Email validation regex
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

});