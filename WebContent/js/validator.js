$(function() {
  $("form[name='add']").validate({
    rules: {
      computerName: "required"
    },
    introduced:{
        required:false
    },
    discontinued:{
        required:false,
    },
    messages: {
      computerName: "Entrer le nom du computer",
      discontinued: "Doit etre superieur à introduced"
    },
    submitHandler: function(form) {
      form.submit();
    }
  });
});
