<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

 <jstl:choose>
 <jstl:when test="${pageContext.response.locale.language=='en'}">
				<b> TERMS AND CONDITIONS </b>
 
<p> This document sets out the terms on which Corpore-Fit uses and protects the information that is provided by its users when using your website. This company is committed to the security of its users' data. When we ask you to fill in the fields of personal information with which you can be identified, we do so by ensuring that it will only be used in accordance with the terms of this document. However, these Terms of Use may change over time or be updated for what we recommend and we strongly encourage you to continually review this page to ensure that you agree to such changes. <P>
<i> <b> Information that is collected </b> </i>

<p> Our website may collect personal information such as: Name, contact information such as your email address and demographic information. Also when necessary, specific information may be required to process an order or make a delivery or billing. </p>
<i> <b> Use of the information collected </b> </i>

<p> Our website uses the information in order to provide the best possible service, particularly to maintain a register of users, orders if applicable, and improve our products and services. It is possible that periodic emails will be sent through our site with special offers, new products and other advertising information that we consider relevant to you or that may benefit you, these emails will be sent to the address you provide and may be canceled. anytime.

Corpore-Fit is highly committed to fulfilling the commitment to keep your information secure. We use the most advanced systems and update them constantly to ensure that there is no unauthorized access. </p>
<i> <b> Cookies </b> </i>

<p> A cookie refers to a file that is sent for the purpose of requesting permission to be stored on your computer, when accepting said file is created and the cookie then serves to have information regarding web traffic, and also facilitates future visits to a recurring website. Another function that cookies have is that with them the web can recognize you individually and therefore provide you with the best personalized service on your website.

Our website uses cookies to identify the pages that are visited and their frequency. This information is used only for statistical analysis and then the information is permanently deleted. You can delete cookies at any time from your computer. However, cookies help to provide a better service to websites, you are not given access to information from your computer or from you, unless you want it and provide it directly. You can accept or deny the use of cookies, however most browsers automatically accept cookies because it serves to have a better web service. You can also change the configuration of your computer to decline cookies. If they decline, you may not be able to use some of our services. </p>

<i> <b> Links to Third Parties </b> </i>

<p> This website may contain links to other sites that may be of interest to you. Once you click on these links and leave our page, we no longer have control over the site to which you are redirected and therefore we are not responsible for the terms or privacy or the protection of your data in those other third party sites. These sites are subject to their own privacy policies, so it is recommended that you check them to confirm that you agree with them. </p>

<i> <b> Control of your personal information </b> </i>

<p>You can edit the information provided to the system at any time by clicking on the personal edit button on your profile and deleting your account using the delete account button. 
At any time you can restrict the collection or use of personal information that is provided to our website. Each time you are asked to fill in a form, such as the user registration form, you can check or uncheck the option to receive information by email. In case you have marked the option to receive our newsletter or advertising you can cancel it at any time.

A manager with an assigned gym won't be able to delete his/her account until a sustitute has been choosen by an administrator or the gym has been deleted.

This company will not sell, assign or distribute personal information that is collected without your consent, unless required by a judge with a court order. </p>

<p> Corpore-Fit reserves the right to change the terms and conditions at any time. </p>

 </jstl:when>
 <jstl:otherwise>

                <b>TERMINOS Y CONDICIONES</b>
 
<p>El presente documento establece los t�rminos en que Corpore-Fit usa y protege la informaci�n que es proporcionada por sus usuarios al momento de utilizar su sitio web. Esta compa��a est� comprometida con la seguridad de los datos de sus usuarios. Cuando le pedimos llenar los campos de informaci�n personal con la cual usted pueda ser identificado, lo hacemos asegurando que s�lo se emplear� de acuerdo con los t�rminos de este documento. Sin embargo estos Terminos de uso pueden cambiar con el tiempo o ser actualizados por lo que le recomendamos y enfatizamos revisar continuamente esta p�gina para asegurarse que est� de acuerdo con dichos cambios.<p>

<i><b>Informaci�n que es recogida</b></i>

<p>Nuestro sitio web podr� recoger informaci�n personal por ejemplo: Nombre,  informaci�n de contacto como  su direcci�n de correo electr�nica e informaci�n demogr�fica. As� mismo cuando sea necesario podr� ser requerida informaci�n espec�fica para procesar alg�n pedido o realizar una entrega o facturaci�n.</p>

<i><b>Uso de la informaci�n recogida</b></i>

<p>Nuestro sitio web emplea la informaci�n con el fin de proporcionar el mejor servicio posible, particularmente para mantener un registro de usuarios, de pedidos en caso que aplique, y mejorar nuestros productos y servicios.  Es posible que sean enviados correos electr�nicos peri�dicamente a trav�s de nuestro sitio con ofertas especiales, nuevos productos y otra informaci�n publicitaria que consideremos relevante para usted o que pueda brindarle alg�n beneficio, estos correos electr�nicos ser�n enviados a la direcci�n que usted proporcione y podr�n ser cancelados en cualquier momento.

Corpore-Fit est� altamente comprometido para cumplir con el compromiso de mantener su informaci�n segura. Usamos los sistemas m�s avanzados y los actualizamos constantemente para asegurarnos que no exista ning�n acceso no autorizado.</p>

<i><b>Cookies</b></i>

<p>Una cookie se refiere a un fichero que es enviado con la finalidad de solicitar permiso para almacenarse en su ordenador, al aceptar dicho fichero se crea y la cookie sirve entonces para tener informaci�n respecto al tr�fico web, y tambi�n facilita las futuras visitas a una web recurrente. Otra funci�n que tienen las cookies es que con ellas las web pueden reconocerte individualmente y por tanto brindarte el mejor servicio personalizado de su web.

Nuestro sitio web emplea las cookies para poder identificar las p�ginas que son visitadas y su frecuencia. Esta informaci�n es empleada �nicamente para an�lisis estad�stico y despu�s la informaci�n se elimina de forma permanente. Usted puede eliminar las cookies en cualquier momento desde su ordenador. Sin embargo las cookies ayudan a proporcionar un mejor servicio de los sitios web, est�s no dan acceso a informaci�n de su ordenador ni de usted, a menos de que usted as� lo quiera y la proporcione directamente. Usted puede aceptar o negar el uso de cookies, sin embargo la mayor�a de navegadores aceptan cookies autom�ticamente pues sirve para tener un mejor servicio web. Tambi�n usted puede cambiar la configuraci�n de su ordenador para declinar las cookies. Si se declinan es posible que no pueda utilizar algunos de nuestros servicios.</p>

<i><b>Enlaces a Terceros</b></i>

<p>Este sitio web pudiera contener en laces a otros sitios que pudieran ser de su inter�s. Una vez que usted de clic en estos enlaces y abandone nuestra p�gina, ya no tenemos control sobre al sitio al que es redirigido y por lo tanto no somos responsables de los t�rminos o privacidad ni de la protecci�n de sus datos en esos otros sitios terceros. Dichos sitios est�n sujetos a sus propias pol�ticas de privacidad por lo cual es recomendable que los consulte para confirmar que usted est� de acuerdo con estas.</p>

<i><b>Control de su informaci�n personal</b></i>

<p>Puede editar la informaci�n proporcionada al sistema en cualquier momento mediante el bot�n de editar personales que se encuentra en su perfil as� como borrar su cuenta mediante el boton de borrar cuenta.
En cualquier momento usted puede restringir la recopilaci�n o el uso de la informaci�n personal que es proporcionada a nuestro sitio web.  Cada vez que se le solicite rellenar un formulario, como el de alta de usuario, puede marcar o desmarcar la opci�n de recibir informaci�n por correo electr�nico.  En caso de que haya marcado la opci�n de recibir nuestro bolet�n o publicidad usted puede cancelarla en cualquier momento.

Un manager que tenga un gimnasio asignado no podr� borrar su cuenta a menos que un administrador lo desasigne de �ste o lo borre.

Esta compa��a no vender�, ceder� ni distribuir� la informaci�n personal que es recopilada sin su consentimiento, salvo que sea requerido por un juez con un orden judicial.</p>

<p>Corpore-Fit Se reserva el derecho de cambiar los t�rminos y condiciones en cualquier momento.</p>


 </jstl:otherwise>
 </jstl:choose>


