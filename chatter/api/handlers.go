package api

import (
	"github.com/npathai/chatter/web"
	"net/http"
)

func (api *API) ApiHandler(h func(*web.Context, http.ResponseWriter, *http.Request)) http.Handler {
	return &web.Handler{
		GetGlobalAppOptions: api.GetGlobalAppOptions,
		HandlerFunc: h,
	}
}
