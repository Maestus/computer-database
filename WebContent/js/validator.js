$(function() {
  $("form[name='add']").validate({
    rules: {
      computerName: "required"
    },
    messages: {
      computerName: "Entrer le nom du computer",
    },
    submitHandler: function(form) {
      form.submit();
    }
  });
});
