<?xml version="1.0" encoding="UTF-8" ?>
<persistence
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
	version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">

	<persistence-unit name="taller-provider"
		transaction-type="RESOURCE_LOCAL">
		<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>

		<class>pojos.UsuarioJPA</class>
		<class>pojos.Rol</class>

		<properties>
			<!-- Connection properties -->
			<property name="javax.persistence.jdbc.driver"
				value="org.sqlite.JDBC" />
			<property name="javax.persistence.jdbc.url"
				value="jdbc:sqlite:./db/coche.db" />
			<!-- JPA doesn't create the schema -->
			<property name="eclipselink.ddl-generation"
				value="create-tables" />
		</properties>
	</persistence-unit>
</persistence>