function hideAndShowPass() {
	var passwordField = document.getElementById("password");
	var eyeIcon = document.querySelector(".eye-icon");

	if (passwordField.type === "password") {
		passwordField.type = "text";
		eyeIcon.classList.remove("fa-eye-slash");
		eyeIcon.classList.add("fa-eye");
	} else {
		passwordField.type = "password";
		eyeIcon.classList.remove("fa-eye");
		eyeIcon.classList.add("fa-eye-slash");
	}
}

function closeAlert(button) {
	var alert = button.closest('.alert');
	if (alert) {
		alert.remove();
	}
}

document.addEventListener("DOMContentLoaded", () => {
    var form = document.getElementById("login-form");

    form.onsubmit = function() {
        var email = form.elements["email"].value;
        var password = form.elements["password"].value;

        var emailError = form.querySelector('.email-error');
        var passwordError = form.querySelector('.password-error');

        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        var passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$/;

        var isEmailValid = emailRegex.test(email);
        var isPasswordValid = passwordRegex.test(password);

        var errors = [];

        if (!isEmailValid) {
            errors.push('Invalid email format.');
        }

        if (!isPasswordValid) {
            errors.push('Password must contain at least 8 characters including lowercase, uppercase, digit, and special character.');
        }

        if (errors.length > 0) {
            errors.forEach(error => {
                if (error.includes('email')) {
                    emailError.innerHTML = error;
                } else if (error.includes('Password')) {
                    passwordError.innerHTML = error;
                }
            });
            return false; // Prevent form submission
        }

        return true; // Allow form submission
    };
});



