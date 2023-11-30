import { RootRoute, Route, Router } from "@tanstack/react-router";
import { Home, Login, Signup } from "./pages";

const rootRoute = new RootRoute();

const indexRoute = new Route({
    getParentRoute: () => rootRoute,
    path: "/",
    component: Home,
});

const loginRoute = new Route({
    getParentRoute: () => rootRoute,
    path: "login",
    component: Login,
});

const signupRoute = new Route({
    getParentRoute: () => rootRoute,
    path: "signup",
    component: Signup,
});

const listsRoute = new Route({
    getParentRoute: () => rootRoute,
    path: "list",
});

const listRoute = new Route({
    getParentRoute: () => listsRoute,
    path: "$uuid",
    component: Home,
});

const routeTree = rootRoute.addChildren([
    indexRoute,
    loginRoute,
    signupRoute,
    listsRoute.addChildren([listRoute]),
]);

const router = new Router({ routeTree, defaultPreload: "intent" });

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router;
    }
}

export { router };
