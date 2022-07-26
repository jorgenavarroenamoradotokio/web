function validarAltaPersona() {
	var nombre = $("#name").val();
	var apellido = $("#surname").val();
	var type = $("#typePerson").val();
	
	if (validarNotEmptyAndNull(nombre) && validarNotEmptyAndNull(apellido) && validarNotEmptyAndNull(type)) {
		$("#username").removeClass("is-invalid");
		$("#password").removeClass("is-invalid");
		$("#name").removeClass("is-invalid");
		$("#frmAltaPersona").submit();
	} else {
		if (validarNotEmptyAndNull(nombre)) {
			$("#name").removeClass("is-invalid");
		} else {
			$("#name").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(apellido)) {
			$("#surname").removeClass("is-invalid");
		} else {
			$("#surname").addClass("is-invalid");
		}

		if (validarNotEmptyAndNull(type)) {
			$("#typePerson").removeClass("is-invalid");
		} else {
			$("#typePerson").addClass("is-invalid");
		}
	}
}