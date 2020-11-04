package main

import (
	"github.com/npathai/chatter/cmd/chatter/commands"
	"os"
)

func main() {
	if err := commands.Run(os.Args[1:]); err != nil {
		os.Exit(1)
	}
}
