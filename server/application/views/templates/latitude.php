<!doctype html>
<html>
<head><link rel='stylesheet' href='style.css' /></head>
<body>
<header><h1>Google Latitude Sample App</h1></header>
<div class="box">
  <?php if(isset($currentLocation)): ?>
    <div class="currentLocation">
      <pre><?php var_dump($currentLocation); ?></pre>
    </div>
  <?php endif ?>

  <?php if (isset($location)): ?>
    <div class="location">
      <pre><?php var_dump($location); ?></pre>
    </div>
  <?php endif ?>

  <?php
    if(isset($authUrl)) {
      print "<a class='login' href='$authUrl'>Connect Me!</a>";
    } else {
     print "<a class='logout' href='?logout'>Logout</a>";
    }
  ?>
</div>
</body></html>