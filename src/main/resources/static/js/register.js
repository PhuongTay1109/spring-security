
document.addEventListener("DOMContentLoaded", async () => {
	var form = document.getElementById("register-form");
	form.addEventListener("submit", async (e) => {
		e.preventDefault();
		
		var emailError = form.querySelector('.email-error');
    	var passwordError = form.querySelector('.password-error');

		const firstName = form.querySelector('input[name="firstName"]').value;
        const lastName = form.querySelector('input[name="lastName"]').value;
        const password = form.querySelector('input[name="password"]').value;
        const email = form.querySelector('input[name="email"]').value;
        
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
            return;
        }

		try {
			const response = await fetch("/api/process_register", {
				method: "POST",
				body: JSON.stringify({email, password, firstName, lastName}),
				headers: {'Content-Type': 'application/json'}
			});

			const data = await response.json();
			
			if (response.ok) {
                if (data.success)
                    location.assign("/login?registered");
            } 
            else if (response.status === 400) {
				var existMessage = document.getElementById("exist-message");
			    existMessage.classList.add("alert", "alert-primary", "alert-dismissible");
			    existMessage.innerHTML = `
			        A user account with this email already exists.
			        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
			            <span aria-hidden="true">&times;</span>
			        </button>`;

			    var closeButton = existMessage.querySelector('.close');
			    closeButton.addEventListener('click', function() {			  
			        existMessage.classList.remove();
			        existMessage.innerHTML='';
   				});
            }
		}
		catch (error) {
			console.log(error);			
		}	
	})
})

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