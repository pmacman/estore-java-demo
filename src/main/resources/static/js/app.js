// App Styles
import "../css/site.css";

// Vendor Scripts and Styles
import "jquery";
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootswatch/dist/superhero/bootstrap.min.css";
import "datatables.net";
import "datatables.net-bs4";
import "datatables.net-bs4/css/dataTables.bootstrap4.css";

// App Scripts
import { CartModule } from "./cart";
import { CustomerModule } from "./customer";
import { LocaleModule } from "./locale";
import { PartnerModule } from "./partner";
import { SearchModule } from "./search";

// Assign to global "App" variable which makes modules accessible from web pages.
window.App = {
    CartModule: CartModule,
    CustomerModule: CustomerModule,
    LocaleModule: LocaleModule,
    PartnerModule: PartnerModule,
    SearchModule: SearchModule
};