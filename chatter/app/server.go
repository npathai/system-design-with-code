package app

import (
	"context"
	"fmt"
	"github.com/gorilla/mux"
	"github.com/npathai/chatter/store"
	"net/http"
	"time"
)

type Server struct {
	Router *mux.Router
	Server *http.Server
	Store store.Store
}

func NewServer() (*Server, error) {
	Srv := &Server{}
	Srv.Router = mux.NewRouter()
	return Srv, nil
}

func (Srv *Server) Start() {
	fmt.Println("Starting server")
	Srv.Server = &http.Server{
		Addr: "127.0.0.1:8080",
		Handler: Srv.Router,
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second,
	}
	if err := Srv.Server.ListenAndServe(); err != nil {
		panic("Couldn't start web server")
	}
}

func (Srv *Server) Stop() {
	if err := Srv.Server.Shutdown(context.Background()); err != nil {
		fmt.Printf("Error in stopping server: %v\n", err)
	} else {
		fmt.Println("Server stopped")
	}
}
