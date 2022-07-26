function validarLogin() {
	var userName = $("#username").val();
	var password = $("#password").val();
	if (validarNotEmptyAndNull(userName) && validarNotEmptyAndNull(password)) {
		$("#username").removeClass("is-invalid");
		$("#password").removeClass("is-invalid");
		$("#frmLogin").submit();
	} else {
		if (validarNotEmptyAndNull(userName)) {
			$("#username").removeClass("is-invalid");
		} else {
			$("#username").addClass("is-invalid");
		}
		if (validarNotEmptyAndNull(password)) {
			$("#password").removeClass("is-invalid");
		} else {
			$("#password").addClass("is-invalid");
		}
	}
}

function enviarAlta(envioAdmin) {
	var registroSencillo = validarRegistro();
	if (envioAdmin && validarRegistroAdmin() && registroSencillo) {
		$("#frmAltaPersona").submit();
	}else if (!envioAdmin && registroSencillo){
		$("#frmAltaPersona").submit();
	}
}

function validarRegistro() {
	var userName = $("#username").val();
	var password = $("#password").val();
	var name = $("#name").val();
	var surname = $("#surname").val();
	var email = $("#email").val();
	var birthday = $("#birthDate").val();
	var validado = false;

	if (validarNotEmptyAndNull(userName) && validarNotEmptyAndNull(password) && validarNotEmptyAndNull(name)
		&& validarNotEmptyAndNull(surname) && validarNotEmptyAndNull(email) && validarNotEmptyAndNull(birthday)) {
		$("#username").removeClass("is-invalid");
		$("#password").removeClass("is-invalid");
		$("#name").removeClass("is-invalid");
		$("#surname").removeClass("is-invalid");
		$("#email").removeClass("is-invalid");
		$("#birthDate").removeClass("is-invalid");
		
		validado = true;
	} else {
		if (validarNotEmptyAndNull(userName)) {
			$("#username").removeClass("is-invalid");
		} else {
			$("#username").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(password)) {
			$("#password").removeClass("is-invalid");
		} else {
			$("#password").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(name)) {
			$("#name").removeClass("is-invalid");
		} else {
			$("#name").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(surname)) {
			$("#surname").removeClass("is-invalid");
		} else {
			$("#surname").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(email)) {
			$("#email").removeClass("is-invalid");
		} else {
			$("#email").addClass("is-invalid");
		}
		
		if (validarNotEmptyAndNull(birthday)) {
			$("#birthDate").removeClass("is-invalid");
		} else {
			$("#birthDate").addClass("is-invalid");
		}
	}
	
	return validado;
}

function validarRegistroAdmin() {
	var rol = $("#rol").val();
	var validado = false;
	if (validarNotEmptyAndNull(rol)) {
		$("#rol").removeClass("is-invalid");
		validado = true;
	} else {
		if (validarNotEmptyAndNull(rol)) {
			$("#rol").removeClass("is-invalid");
		} else {
			$("#rol").addClass("is-invalid");
		}
	}
	return validado;
}