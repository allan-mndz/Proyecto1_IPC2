import { Routes } from '@angular/router';
import { Login } from './componentes/login/login';
import { Dashboard } from './componentes/dashboard/dashboard';
import { Destinos } from './componentes/destinos/destinos';
import { Proveedores } from './componentes/proveedores/proveedores'; 
import { Paquetes } from './componentes/paquetes/paquetes';
import { Clientes } from './componentes/clientes/clientes';
import { Reservaciones } from './componentes/reservaciones/reservaciones'; 
import { Pagos } from './componentes/pagos/pagos';
import { Usuarios } from './componentes/usuarios/usuarios';
import { Reportes } from './componentes/reportes/reportes';
import { CargaDatos } from './componentes/carga-datos/carga-datos';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard, children: [
        {path: 'destinos', component: Destinos},
        {path: 'proveedores', component: Proveedores},
        {path: 'paquetes', component: Paquetes},
        {path: 'clientes', component: Clientes},
        {path: 'reservaciones', component: Reservaciones},
        {path: 'pagos', component: Pagos},
        {path: 'usuarios', component: Usuarios},
        {path: 'reportes', component: Reportes},
        {path: 'cargarDatos', component: CargaDatos}
    ]},
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
