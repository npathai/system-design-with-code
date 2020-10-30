package api

import (
	"github.com/gorilla/mux"
)

type Routes struct {
	Root *mux.Router
	Users *mux.Router
}

type API struct {
	BaseRoutes *Routes
}

func Init(root *mux.Router) *API {
	api := &API{
		BaseRoutes: &Routes{},
	}
	api.BaseRoutes.Root = root
	api.BaseRoutes.Users = api.BaseRoutes.Root.PathPrefix("/users").Subrouter()

	api.InitUser()
	return api
}
