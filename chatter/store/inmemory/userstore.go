package inmemory

import (
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/store"
)

type MemoryUserStore struct {
	users []*model.User
}

func (muStore *MemoryUserStore) GetAllUsers() ([]*model.User, error) {
	if muStore.users == nil {
		return []*model.User{}, nil
	}
	return muStore.users, nil
}

func (muStore *MemoryUserStore) Save(user *model.User) (*model.User, error) {
	user.Id = model.NewId()
	muStore.users = append(muStore.users, user)
	return user, nil
}

func (muStore *MemoryUserStore) Get(userId string) (*model.User, error) {
	// TODO Use efficient implementation using map
	for _, u := range muStore.users {
		if u.Id == userId {
			return u, nil
		}
	}
	return nil, store.NewErrNotFound("User", userId)
}