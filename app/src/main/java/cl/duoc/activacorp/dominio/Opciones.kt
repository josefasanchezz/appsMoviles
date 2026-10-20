package cl.duoc.activacorp.dominio

/** Catálogos definidos por el equipo para los campos de selección. */
object Opciones {
    val tipos = listOf("Feria sectorial", "Congreso", "Lanzamiento de producto", "Auspicio", "Reunión con cliente", "Workshop corporativo")
    val prioridades = listOf("Alta", "Media", "Baja")
    val potenciales = listOf("Muy alto", "Alto", "Moderado", "Bajo")
    val estados = listOf("Evento identificado", "Analizando factibilidad", "Primer contacto", "Propuesta presentada", "Ganada", "Descartada")
}
