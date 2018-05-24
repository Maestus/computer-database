
$(function() {
  $("form[name='computerForm']").validate({
    rules: {
      computerName: "required",
      introduced: "date",
      discontinued: "date"
    },
    messages: {
      computerName: "Entrer le nom du computer",
      discontinued: "Doit etre superieur a introduced"
    },
    submitHandler: function(form) {
      form.submit();
    }
  });
})