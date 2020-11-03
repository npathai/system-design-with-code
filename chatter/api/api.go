package api

import (
	"github.com/gorilla/mux"
	"github.com/npathai/chatter/app"
)

type Routes struct {
	Root *mux.Router
	Users *mux.Router
}

type API struct {
	GetGlobalAppOptions app.AppOptionCreator
	BaseRoutes *Routes
}

func Init(globalOptionsFunc app.AppOptionCreator, root *mux.Router) *API {
	api := &API{
		GetGlobalAppOptions: globalOptionsFunc,
		BaseRoutes: &Routes{},
	}
	api.BaseRoutes.Root = root
	api.BaseRoutes.Users = api.BaseRoutes.Root.PathPrefix("/users").Subrouter()

	api.InitUser()
	return api
}
