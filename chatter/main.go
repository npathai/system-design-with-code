package main

import (
	"fmt"
	"github.com/npathai/chatter/api"
	"github.com/npathai/chatter/app"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	fmt.Println("Welcome to Chatter, the single place for all your communications")
	s, err := app.NewServer()
	if err != nil {
		panic("couldn't start server")
	}

	api.Init(s.Router)
	s.Start()

	ch := make(chan os.Signal)
	signal.Notify(ch, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)

	// Block on channel read till there is interrupt/terminate signal
	<- ch
	s.Stop()
}