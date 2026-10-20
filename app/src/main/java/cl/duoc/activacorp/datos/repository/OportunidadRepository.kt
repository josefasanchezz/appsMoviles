package cl.duoc.activacorp.datos.repository

import cl.duoc.activacorp.datos.local.OportunidadDao
import cl.duoc.activacorp.datos.local.OportunidadEntity
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(private val dao: OportunidadDao) {
    fun observarTodas(): Flow<List<OportunidadEntity>> = dao.observarTodas()
    suspend fun obtener(id: Long): OportunidadEntity? = dao.obtener(id)
    suspend fun eliminar(o: OportunidadEntity) = dao.eliminar(o)
    suspend fun guardar(o: OportunidadEntity) = dao.guardar(o.copy(actualizadoEn = System.currentTimeMillis()))
}
