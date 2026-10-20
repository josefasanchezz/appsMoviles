package cl.duoc.activacorp.ui.oportunidad

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.activacorp.datos.local.OportunidadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOportunidadesScreen(
    vm: OportunidadViewModel, alAgregar: () -> Unit, alEditar: (Long) -> Unit, alVolver: () -> Unit
) {
    val total by vm.oportunidades.collectAsStateWithLifecycle()
    val lista by vm.oportunidadesFiltradas.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.filtroPrioridad.collectAsStateWithLifecycle()
    var porEliminar by remember { mutableStateOf<OportunidadEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oportunidades · ${lista.size}/${total.size}") },
                navigationIcon = { IconButton(onClick = alVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver al menú") } }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = alAgregar, icon = { Icon(Icons.Filled.Add, null) }, text = { Text("Registrar") })
        }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            OutlinedTextField(
                value = busqueda, onValueChange = vm::onBusqueda, singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por nombre, tipo o fuente") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                trailingIcon = { if (busqueda.isNotEmpty()) IconButton(onClick = { vm.onBusqueda("") }) { Icon(Icons.Filled.Clear, "Limpiar búsqueda") } }
            )
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = filtro == null, onClick = { vm.onFiltroPrioridad(null) }, label = { Text("Todas") })
                listOf("Alta", "Media", "Baja").forEach { p ->
                    FilterChip(selected = filtro == p, onClick = { vm.onFiltroPrioridad(if (filtro == p) null else p) }, label = { Text(p) })
                }
            }
            if (lista.isEmpty()) {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Lightbulb, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.outline)
                    Text(
                        if (total.isEmpty()) "Aún no hay oportunidades registradas" else "Sin resultados para el filtro actual",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(lista, key = { it.id }) { o ->
                        Tarjeta(o, { alEditar(o.id) }, { porEliminar = o }, Modifier.animateItem())
                    }
                }
            }
        }
    }

    porEliminar?.let { o ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar oportunidad") },
            text = { Text("¿Eliminar «${o.nombre}»? Esta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = { vm.eliminar(o); porEliminar = null }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun Tarjeta(o: OportunidadEntity, alEditar: () -> Unit, alEliminar: () -> Unit, modifier: Modifier = Modifier) {
    val objetivo = when (o.prioridad) { "Alta" -> Color(0xFFD32F2F); "Media" -> Color(0xFFEF6C00); else -> Color(0xFF388E3C) }
    val color by animateColorAsState(objetivo, label = "prioridad")
    Card(onClick = alEditar, modifier = modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(o.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${o.tipo} · Potencial ${o.potencial.lowercase()}", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("● ${o.prioridad}", color = color, style = MaterialTheme.typography.labelLarge)
                    SuggestionChip(onClick = alEditar, label = { Text(o.estado) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (o.fotoPath != null) Icon(Icons.Filled.PhotoCamera, "Con foto", Modifier.size(16.dp))
                    if (o.latitud != null) Icon(Icons.Filled.LocationOn, "Con ubicación", Modifier.size(16.dp))
                }
            }
            IconButton(onClick = alEliminar) { Icon(Icons.Filled.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) }
        }
    }
}
