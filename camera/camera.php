<?php
  define('BASE_DIR', dirname(__FILE__));
  require_once(BASE_DIR.'/config.php');
?>
<html>
  <head>
    <meta name="viewport" content="width=550, initial-scale=1">
    <link rel="stylesheet" href="css/style_minified.css" />
    <script src="js/style_minified.js"></script>
    <script src="js/script.js"></script>
    <script>
            //
        // Interface
        //
        function telaCheia() {

          var background = document.getElementById("background");
          var projecao = document.getElementById("mjpeg_dest");

          if(!background) {
            background = document.createElement("div");
            background.id = "background";
            document.body.appendChild(background);
          }

          projecao.className = "fullscreen";
          background.style.display = "block";

        }
    </script>
  </head>
  <body onload="setTimeout('init(); telaCheia()', 100);">
      <img id="mjpeg_dest" onclick="toggle_fullscreen(this);" src="cam_pic.php">
  </body>
</html>