package app

import (
	"context"
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
	"time"
)

type Server struct {
	Router *mux.Router
	Server *http.Server
}

func NewServer() {
	Srv = &Server{}
	Srv.Router = mux.NewRouter()
}

func StartServer() {
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

func StopServer() {
	if err := Srv.Server.Shutdown(context.Background()); err != nil {
		fmt.Printf("Error in stopping server: %v\n", err)
	} else {
		fmt.Println("Server stopped")
	}
}
