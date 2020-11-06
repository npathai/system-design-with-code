package store

import (
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/store/inmemory"
)

type Store interface {
	User() UserStore
}

type UserStore interface {
	Save(user *model.User) (*model.User, error)
	GetAllUsers() ([]*model.User, error)
	Get(userId string) (*model.User, error)
}

type LocalStore struct {
	uStore inmemory.MemoryUserStore
}

func (store *LocalStore) User() UserStore {
	return &store.uStore
}

func NewStore() *LocalStore {
	store := &LocalStore{}
	store.uStore = inmemory.MemoryUserStore{}
	return store
}