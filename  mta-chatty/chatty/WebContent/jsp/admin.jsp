<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

    <section id="AdminPage" data-role="page" data-add-back-btn="true">

		<script src="http://www.pureexample.com/js/flot/excanvas.min.js"></script>
		<script src="http://www.pureexample.com/js/flot/jquery.flot.min.js"></script>

		<header data-role="header" class="adminHeader">
			<label class="header-label">Chatty Admin</label>
			<img   class="header-image" src="image/Admin.png" alt="Admin"/>
		</header>

        <div class="adnimContent" data-role="content">

			<div>
				<h1>Buddies:  </h1>
				<h1>Groups:   </h1>
				<div id="flot_buddies_groups" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
				<br><br>
				<h1>Messages: </h1>
				<div id="flot_messages_per_day" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
			</div>
        </div>		

	</section>
        