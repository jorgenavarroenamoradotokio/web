function buscarPeliculaPorTitulo(){
	var seachTitle = $("#buscador").val();
	if (validarNotEmptyAndNull(seachTitle)) {
		$("#buscador").removeClass("is-invalid");
		$("#frmSearchFilmTitle #titulo").val(seachTitle);
		$("#frmSearchFilmTitle").submit();
	} else {
		$("#buscador").addClass("is-invalid");
	}	
}

function buscarPeliculaPorLetra(letter){
	$.ajax({
		type: "GET",
		contentType : "application/json",
		url: "ajax/search",
		data:{"letter" : letter},
		dataType : 'json',
		beforeSend: function() {
			$('#modal').modal('show');
		},
		success: function(result) {
			$('#resultadoBusqueda').empty();
			for (var i = 0; i < result.data.length; i++) {
				var data = result.data[i];
				var article = "<article class='postcard dark blue mt-5'>"
					+ "<img class='postcard__img' src='../images/poster/" + data.image + "' alt='" + data.title + "'/>"
					+ "<div class='postcard__text'>"
					+ "<h1 class='postcard__title blue'>"
					+ "<a href='/view-film/"+data.id+"'>"+data.title+"</a>"
					+ "</h1>"
					+ "<p class='postcard__preview-txt'>"+data.sypnosis+"</p>"
					+ "<ul class='postcard__tagbox'>"
					+ "<li class='tag__item'><i class='fas fa-clock mr-2'></i><span> "+data.duration+" mins. </span></li>"
					+ "</ul>"
					+ "</div>"
					+ "</article>";
				$("#resultadoBusqueda").append(article);
			}
			$('#modal').modal('hide');			
		},
		error: function(e) { 
			$('#modal .modal-body').empty();
			$('#modal .modal-footer').empty();
			$('#modal .modal-body').append("<div class='alert alert-danger' role='alert'>Error</div>");
			$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
			console.log("ERROR: ", e);
		}
	});
}
		
function puntuacion() {
	// Agregamos la puntuacion al formulario
	$("#frmScore #points").val( $('input[name="rating"]:checked').val());
	
	// Iniciamos la conversion a json
	var f = new FormData(document.getElementById("frmScore"));
	let formDataObject = Object.fromEntries(f.entries());
  	let formDataJsonString = JSON.stringify(formDataObject);
  	
  	// Obtenemos la url del formulario
  	var form = $("#frmScore");
  	var url = form.attr('action');
	$.ajax({
		type: "post",
		contentType : "application/json",
		url: url,
		data: formDataJsonString,
		dataType : 'json',
		beforeSend: function() {
			$('#modal').modal('show');
		},
		success: function(result) {
			$('input[name="rating"]').attr("disabled",true);
			$('#modal .modal-body').empty();
			$('#modal .modal-footer').empty();
			
			if (result.status == "duplicate") {
				$('#modal .modal-body').append("<div class='alert alert-danger' role='alert'>"+result.data+"</div>");
				$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
			} else if (result.status == "success") {
				$("#mediaFilm").text(result.data);
				$('#modal .modal-body').append("<div style='text-align: center;'><i style='color: #008f39;' class='fa-solid fa-circle-check fa-2xl'></i></div>");
				$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
			}		
		},
		error: function(e) { 
			$('#modal .modal-body').empty();
			$('#modal .modal-footer').empty();
			$('#modal .modal-body').append("<div class='alert alert-danger' role='alert'>Error</div>");
			$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
			console.log("ERROR: ", e);
		}
	});
}

function comentario() {
	// Iniciamos la validacion de los datos
	var correcto = true;
	
	var title=$("#title").val();
	var text=$("#textReview").val();
	
	if (title == null || title == "") {
		$("#title").addClass("is-invalid");
		correcto = false;
	} else{
		$("#title").removeClass("is-invalid");
	}
	
	if (text == null || text == "") {
		$("#textReview").addClass("is-invalid");
		correcto = false;
	} else{
		$("#textReview").removeClass("is-invalid");
	}
	
	if (correcto) {		
		// Iniciamos la conversion a json
		var f = new FormData(document.getElementById("frmAltaReview"));
		let formDataObject = Object.fromEntries(f.entries());
	  	let formDataJsonString = JSON.stringify(formDataObject);
	  	
	  	// Obtenemos la url del formulario
	  	var form = $("#frmAltaReview");
	  	var url = form.attr('action');
		$.ajax({
			type: "post",
			contentType : "application/json",
			url: url,
			data: formDataJsonString,
			dataType : 'json',
			beforeSend: function() {
				$('#modal').modal('show');
			},
			success: function(result) {
				$('#modal .modal-body').empty();
				$('#modal .modal-footer').empty();
				
				if (result.status == "duplicate") {
					$('#modal .modal-body').append("<div class='alert alert-danger' role='alert'>"+result.data+"</div>");
					$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
				} else if (result.status == "success") {
					var dateJson = result.data.date;
					var dateJson = dateJson.split("-")[2] + "-" + dateJson.split("-")[1] + "-" + dateJson.split("-")[0];
					var review = "<div class='col-12'><div class='row gy-2'>"
							+"<div class='col-md-12'><span>"+result.data.title+"</span><br>"
							+"<small class='text-muted'>"+dateJson+"</small></div>"
							+"<div class='col-md-12 text-center'><p>"+result.data.textReview+"</p></div></div></div>";
					
					$('#comentarios tbody').prepend('<tr />').children('tr:first').append('<td>'+review+'</td>');
					$('#modal .modal-body').append("<div style='text-align: center;'><i style='color: #008f39;' class='fa-solid fa-circle-check fa-2xl'></i></div>");
					$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
				}
			},
			error: function(e) { 
				$('#modal .modal-body').empty();
				$('#modal .modal-footer').empty();
				$('#modal .modal-body').append("<div class='alert alert-danger' role='alert'>Error</div>");
				$('#modal .modal-footer').append("<button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-door-open'></i></button>");
				console.log("ERROR: ", e);
			}
		});
	}	
}

function buscarPeliculaPorInicial(){
	$("#frmSearchFilmTitleInitial").submit();	
}

function altaPelicula() {
	var title = $("#title").val();
	var year = $("#anio").val();
	var duracion = $("#duracion").val();
	var sypnosis = $("#sypnosis").val();
	
	if (validarNotEmptyAndNull(title) && validarNotEmptyAndNull(year)
		&& validarNotEmptyAndNull(duracion) && validarNotEmptyAndNull(sypnosis)) {
		$("#title").removeClass("is-invalid");
		$("#anio").removeClass("is-invalid");
		$("#duracion").removeClass("is-invalid");
		$("#duracion").removeClass("is-invalid");
		$("#frmAltaFilm").submit();
	} else {
		if (validarNotEmptyAndNull(title)) {
			$("#title").removeClass("is-invalid");
		} else {
			$("#title").addClass("is-invalid");
		}
		if (validarNotEmptyAndNull(year) && year > 0) {
			$("#anio").removeClass("is-invalid");
		} else {
			$("#anio").addClass("is-invalid");
		}
		if (validarNotEmptyAndNull(duracion) && duracion > 0) {
			$("#duracion").removeClass("is-invalid");
		} else {
			$("#duracion").addClass("is-invalid");
		}
		if (validarNotEmptyAndNull(sypnosis)) {
			$("#duracion").removeClass("is-invalid");
		} else {
			$("#sypnosis").addClass("is-invalid");
		}
	}
}


function seleccionarImagen() {
	let reader = new FileReader();
	var documento = $('#exampleFormControlFile1').prop('files')[0];
	reader.readAsDataURL(documento);
	
	reader.onload = function() {
		$("#imagenPrevisualizacion").attr("src",reader.result);
		$("#imagenPrevisualizacion").attr("width","75%");
		$("#imagenPrevisualizacion").attr("height","75%");
		$("#descripcionImagen").show();
		$("#nombreImagen").text(documento.name);
	};
}

function eliminarImagen(){
	$('#exampleFormControlFile1').val("");
	$("#imagenPrevisualizacion").attr("src","");
	$("#imagenPrevisualizacion").attr("width","");
	$("#imagenPrevisualizacion").attr("height","");
	$("#descripcionImagen").hide();
}